package com.myattheingi.wexchanger.presentation.currency_converter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.myattheingi.wexchanger.R
import com.myattheingi.wexchanger.presentation.MainViewModel
import com.myattheingi.wexchanger.presentation.components.AmountInputTextField
import com.myattheingi.wexchanger.presentation.components.CircularIconButton
import com.myattheingi.wexchanger.presentation.components.DynamicSelectTextField
import com.myattheingi.wexchanger.presentation.components.PrettyProgressBar
import com.myattheingi.wexchanger.presentation.theme.LocalTopAppBarColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(vm: MainViewModel) {
    val uiState by vm.uiState.collectAsState()

    val topBarColors = LocalTopAppBarColors.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(vm.event) {
        vm.event.collect { event ->
            when (event) {
                is MainViewModel.Event.Failure -> {
                    snackbarHostState.showSnackbar(event.throwable.message ?: "Error occurred")
                }

                is MainViewModel.Event.Success -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.title_tool_bar))
                },
                colors = topBarColors
            )
        }
    ) { innerPadding ->

        if (uiState.isLoading) {
            PrettyProgressBar()

        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(dimensionResource(id = R.dimen.gap_16)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Box(
                    contentAlignment = Alignment.Center, // Center the overlapping button
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
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
                            onValueChangedEvent = { vm.onFromCurrencyInfoSelected(it) }
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.gap_32)))
                        DynamicSelectTextField(
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
                            onValueChangedEvent = { vm.onToCurrencyInfoSelected(it) }
                        )
                    }

                    // Overlapping CircularIconButton
                    CircularIconButton(
                        onClick = { vm.onCurrencyReverseClicked() },
                        icon = Icons.Filled.SwapHoriz,
                        contentDescription = "Reverse currencies",
                        modifier = Modifier
                            .align(Alignment.Center) // Center between the two DropDownSelectors
                            .zIndex(1f) // Ensure the button is above the selectors
                    )
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.gap_16)))

                // Amount input field
                AmountInputTextField(
                    value = uiState.fromAmount ?: "",
                    onValueChanged = { vm.onAmountEntered(it) }
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.gap_16)))

                // Calculate exchange rate button
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { vm.calculateExchangeRate() },
                    enabled = uiState.isCalculateEnabled
                ) {
                    Text(stringResource(id = R.string.calculate_exchange_rate))
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.gap_16)))

                // Exchange rate result
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Gray)) {
                            append("${uiState.fromAmount} ${uiState.fromCurrencyInfo.name} =\n")
                        }
                        withStyle(SpanStyle(color = Color.Black)) {
                            append("${uiState.exchangeRate} ${uiState.toCurrencyInfo.name}")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.gap_16)))

                Text(
                    text = stringResource(
                        id = R.string.currency_rate,
                        uiState.fromCurrencyInfo.code,
                        uiState.unitRateFromTo,
                        uiState.toCurrencyInfo.code
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(
                        id = R.string.currency_rate,
                        uiState.toCurrencyInfo.code,
                        uiState.unitRateToFrom,
                        uiState.fromCurrencyInfo.code
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

