package com.myattheingi.wexchanger.presentation.home

import com.myattheingi.wexchanger.core.domain.models.CurrencyExchange
import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo

data class UiState(
    val currencies: List<CurrencyInfo>,
    val fromCurrencyInfo: CurrencyInfo,
    val fromAmount: Double?,
    val exchangeRates: List<CurrencyExchange>,

    val isLoading: Boolean,
    val isCalculateEnabled: Boolean,
) {
    constructor() : this(
        emptyList(),
        CurrencyInfo("MMK", "Myanma Kyat"),
        1.00,
        emptyList(),
        false,
        false,
    )
}