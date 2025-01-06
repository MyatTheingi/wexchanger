package com.myattheingi.wexchanger.core.domain.models

data class CurrencyRate(
    val pair: String,
    val rate: Double
)
data class CurrencyQuotes(
    val quotes: List<CurrencyRate>
)

fun Map<String, Double>.toCurrencyQuotes(): CurrencyQuotes {
    val currencyRates = this.map { (pair, rate) ->
        CurrencyRate(pair, rate)
    }
    return CurrencyQuotes(quotes = currencyRates)
}