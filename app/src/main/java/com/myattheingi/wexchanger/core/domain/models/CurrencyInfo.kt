package com.myattheingi.wexchanger.core.domain.models


data class CurrencyInfo(
    val code: String,
    val name: String
)



fun Map<String, String>.asDomainModel(): List<CurrencyInfo> {
    val currencyInfoList = this.map { (code, name) ->
        CurrencyInfo(code, name)
    }
    return currencyInfoList
}