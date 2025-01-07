package com.myattheingi.wexchanger.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.myattheingi.wexchanger.R
import com.myattheingi.wexchanger.core.utils.toFormattedString
import com.myattheingi.wexchanger.core.utils.toLocaleString
import com.myattheingi.wexchanger.presentation.components.AmountInputTextField
import com.myattheingi.wexchanger.presentation.components.DynamicSelectTextField
import com.myattheingi.wexchanger.presentation.components.ExchangeRateHeader
import com.myattheingi.wexchanger.presentation.components.ExchangeRateItem
import com.myattheingi.wexchanger.presentation.components.PrettyProgressBar

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is HomeViewModel.Event.Failure -> {
                    snackbarHostState.showSnackbar(event.throwable.message ?: "Error occurred")
                }

                is HomeViewModel.Event.Success -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        if (uiState.isLoading) {
            PrettyProgressBar()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(dimensionResource(id = R.dimen.gap_16)),
            ) {
                item {
                    DynamicSelectTextField(
                        selectedValue = uiState.fromCurrencyInfo,
                        options = uiState.currencies,
                        label = R.string.title_from_currency,
                        itemLabelProvider = { currencyInfo ->
                            stringResource(
                                id = R.string.currency_label_format,
                                currencyInfo.code,
                                currencyInfo.name
                            )
                        },
                        onValueChangedEvent = { viewModel.onFromCurrencyInfoSelected(it) }
                    )
                    AmountInputTextField(
                        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.gap_16)),
                        value = uiState.fromAmount.toLocaleString(),
                        onValueChanged = { viewModel.onAmountEntered(it) }
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(id = R.dimen.gap_16)),
                        onClick = { viewModel.calculateExchangeRate() },
                        enabled = uiState.isCalculateEnabled
                    ) {
                        Text(stringResource(id = R.string.calculate_exchange_rate))
                    }
                    ExchangeRateHeader(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.gap_18)))
                    HorizontalDivider()
                }

                // Exchange rates list
                items(uiState.exchangeRates) { exchangeRate ->
                    val backgroundColor =
                        if (uiState.exchangeRates.indexOf(exchangeRate) % 2 == 0) {
                            Color.White
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        }
                    ExchangeRateItem(
                        exchangeRate = exchangeRate,
                        modifier = Modifier.background(backgroundColor)
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
