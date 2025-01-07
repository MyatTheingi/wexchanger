package com.myattheingi.wexchanger.core.domain.models

data class CurrencyRate(
    val pair: String,
    val rate: Double
)


fun Map<String, Double>.asDomainModel(): List<CurrencyRate> {
    val currencyRates = this.map { (pair, rate) ->
        CurrencyRate(pair, rate)
    }
    return currencyRates
}