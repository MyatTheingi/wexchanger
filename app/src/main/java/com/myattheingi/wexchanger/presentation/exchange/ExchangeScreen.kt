package com.myattheingi.wexchanger.presentation.exchange

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.myattheingi.wexchanger.R
import com.myattheingi.wexchanger.core.utils.toFormattedString
import com.myattheingi.wexchanger.core.utils.toLocaleString
import com.myattheingi.wexchanger.presentation.components.AmountInputTextField
import com.myattheingi.wexchanger.presentation.components.CircularIconButton
import com.myattheingi.wexchanger.presentation.components.DynamicSelectTextField
import com.myattheingi.wexchanger.presentation.components.PrettyProgressBar

@Composable
fun ExchangeScreen(viewModel: ExchangeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is ExchangeViewModel.Event.Failure -> {
                    snackbarHostState.showSnackbar(event.throwable.message ?: "Error occurred")
                }

                is ExchangeViewModel.Event.Success -> {
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(dimensionResource(id = R.dimen.gap_16))
                    .verticalScroll(rememberScrollState()),  // Make it scrollable
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
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
                        DynamicSelectTextField(
                            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.gap_32)),
                            selectedValue = uiState.toCurrencyInfo,
                            options = uiState.currencies,
                            label = R.string.title_to_currency,
                            itemLabelProvider = { currencyInfo ->
                                stringResource(
                                    id = R.string.currency_label_format,
                                    currencyInfo.code,
                                    currencyInfo.name
                                )
                            },
                            onValueChangedEvent = { viewModel.onToCurrencyInfoSelected(it) }
                        )
                    }

                    // Overlapping CircularIconButton
                    CircularIconButton(
                        onClick = { viewModel.onCurrencyReverseClicked() },
                        icon = Icons.Filled.SwapHoriz,
                        contentDescription = "Reverse currencies",
                        modifier = Modifier
                            .align(Alignment.Center) // Center between the two DropDownSelectors
                            .zIndex(1f) // Ensure the button is above the selectors
                    )
                }

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

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Gray)) {
                            append("${uiState.fromAmount.toFormattedString()} ${uiState.fromCurrencyInfo.name} =\n")
                        }
                        withStyle(SpanStyle(color = Color.Black)) {
                            append("${uiState.exchangeRate.toFormattedString()} ${uiState.toCurrencyInfo.name}")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = dimensionResource(id = R.dimen.gap_16)),

                    )


                Text(
                    text = stringResource(
                        id = R.string.currency_rate,
                        uiState.fromCurrencyInfo.code,
                        uiState.unitRateFromTo.toFormattedString(),
                        uiState.toCurrencyInfo.code
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.gap_16))
                )

                Text(
                    text = stringResource(
                        id = R.string.currency_rate,
                        uiState.toCurrencyInfo.code,
                        uiState.unitRateToFrom.toFormattedString(),
                        uiState.fromCurrencyInfo.code
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
