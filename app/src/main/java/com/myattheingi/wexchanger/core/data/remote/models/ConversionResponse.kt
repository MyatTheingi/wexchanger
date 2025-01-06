package com.myattheingi.wexchanger.core.data.remote.models


data class ConversionResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val query: Query,
    val result: Double
) {
    data class Query(
        val from: String,
        val to: String,
        val amount: Double
    )
}


/***
 * {
 *     "success": true,
 *     "terms": "https://currencylayer.com/terms",
 *     "privacy": "https://currencylayer.com/privacy",
 *     "query": {
 *         "from": "USD",
 *         "to": "GBP",
 *         "amount": 10
 *     },
 *     "info": {
 *         "timestamp": 1430068515,
 *         "quote": 0.658443
 *     },
 *     "result": 6.58443
 * }
 */
