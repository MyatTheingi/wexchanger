package com.myattheingi.wexchanger.presentation.currency_converter

import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo

data class CurrencyUiState(
    val currencies: List<CurrencyInfo>,
    val fromCurrencyInfo: CurrencyInfo,
    val toCurrencyInfo: CurrencyInfo,
    val fromAmount: String?,
    val exchangeRate: String?,
    val unitRateFromTo: String,
    val unitRateToFrom: String,
    val isLoading: Boolean,
    val isCalculateEnabled: Boolean,
) {
    constructor() : this(
        emptyList(),
        CurrencyInfo("MMK", "Myanma Kyat"),
        CurrencyInfo("USD", "United States Dollar"),
        "1.00",
        "",
        "",
        "",
        false,
        false,
    )
}