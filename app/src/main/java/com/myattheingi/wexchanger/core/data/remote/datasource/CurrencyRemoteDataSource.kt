package com.myattheingi.wexchanger.core.data.remote.datasource

import com.myattheingi.wexchanger.core.data.remote.CurrencyApiService
import javax.inject.Inject

interface CurrencyRemoteDataSource {
    suspend fun getLiveRates(): Map<String, Double>
    suspend fun getCurrencyList(): Map<String, String>
    suspend fun convertCurrency(from: String, to: String, amount: Double): Double
}
class CurrencyRemoteDataSourceImpl @Inject constructor(
    private val service: CurrencyApiService,
) : CurrencyRemoteDataSource {

    override suspend fun getLiveRates(): Map<String, Double> {
        return service.getLiveRates().quotes
    }

    override suspend fun getCurrencyList(): Map<String, String> {
        return service.getCurrencyList().currencies
    }

    override suspend fun convertCurrency(from: String, to: String, amount: Double): Double {
        return service.convertCurrency(
            from = from,
            to = to,
            amount = amount
        ).result
    }
}
