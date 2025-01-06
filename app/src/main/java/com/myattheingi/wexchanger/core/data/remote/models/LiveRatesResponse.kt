package com.myattheingi.wexchanger.core.data.remote.models

data class LiveRatesResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val source: String,
    val quotes: Map<String, Double>
)
/***
 * {
 *     "success": true,
 *     "terms": "https://currencylayer.com/terms",
 *     "privacy": "https://currencylayer.com/privacy",
 *     "timestamp": 1430401802,
 *     "source": "USD",
 *     "quotes": {
 *         "USDAED": 3.672982,
 *         "USDAFN": 57.8936,
 *         "USDALL": 126.1652,
 *         "USDAMD": 475.306,
 *         "USDANG": 1.78952,
 *         "USDAOA": 109.216875,
 *         "USDARS": 8.901966,
 *         "USDAUD": 1.269072,
 *         "USDAWG": 1.792375,
 *         "USDAZN": 1.04945,
 *         "USDBAM": 1.757305,
 *     [...]
 *     }
 * }
 */
