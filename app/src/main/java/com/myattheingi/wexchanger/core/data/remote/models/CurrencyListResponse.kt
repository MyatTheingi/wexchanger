package com.myattheingi.wexchanger.core.data.remote.models


data class CurrencyListResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val currencies: Map<String, String>
)
/**
 * {
 *     "success": true,
 *     "terms": "https://currencylayer.com/terms",
 *     "privacy": "https://currencylayer.com/privacy",
 *     "currencies": {
 *         "AED": "United Arab Emirates Dirham",
 *         "AFN": "Afghan Afghani",
 *         "ALL": "Albanian Lek",
 *         "AMD": "Armenian Dram",
 *         "ANG": "Netherlands Antillean Guilder",
 *         [...]
 *     }
 * }
 * */