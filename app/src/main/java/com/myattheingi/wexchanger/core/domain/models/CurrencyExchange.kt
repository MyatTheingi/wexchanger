package com.myattheingi.wexchanger.core.domain.models

data class CurrencyExchange(
    val code: String,         // e.g., "MMK", "USD", "SDG"
    val unitExchangeRate: Double,    // Exchange rate for 1 unit of `toCurrency` in terms of `fromCurrency`
    val amountExchangeRate: Double  // Exchange rate for the entered amount of `fromCurrency` into `toCurrency`
)

fun Map<String, Pair<Double, Double>>.asDomainModel(): List<CurrencyExchange> {
    return this.map { entry ->
        CurrencyExchange(
            code = entry.key,                        // The currency code (e.g., "MMK", "USD")
            unitExchangeRate = entry.value.first,    // The first value in the Pair (1 unit exchange rate)
            amountExchangeRate = entry.value.second  // The second value in the Pair (amount exchange rate)
        )
    }
}