package com.myattheingi.wexchanger.presentation.exchange

import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo

data class UiState(
    val currencies: List<CurrencyInfo>,
    val fromCurrencyInfo: CurrencyInfo,
    val toCurrencyInfo: CurrencyInfo,
    val fromAmount: Double?,
    val exchangeRate: Double?,
    val unitRateFromTo: Double,
    val unitRateToFrom: Double,
    val isLoading: Boolean,
    val isCalculateEnabled: Boolean,
) {
    constructor() : this(
        emptyList(),
        CurrencyInfo("MMK", "Myanma Kyat"),
        CurrencyInfo("USD", "United States Dollar"),
        1.0,
        0.0,
        0.0,
        0.0,
        false,
        false,
    )
}