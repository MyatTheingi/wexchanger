package com.myattheingi.wexchanger.core.domain.repositories

import com.myattheingi.wexchanger.core.domain.models.CurrencyExchange
import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo
import com.myattheingi.wexchanger.core.domain.models.CurrencyRate
import kotlinx.coroutines.flow.Flow


interface CurrencyRepository {

    // Cache live rates from the remote source
    suspend fun fetchAndStoreLiveRates()

    // Fetch live rates and cache them if necessary
    suspend fun getLiveRates(): Flow<Result<List<CurrencyRate>>>

    // Fetch the currency list and store it locally
    suspend fun fetchAndStoreCurrencyList()

    // Get the stored currency list
    suspend fun getCurrencyList(): Flow<Result<List<CurrencyInfo>>>

    // Convert currency
    suspend fun convertCurrencyToSingle(
        from: String,
        to: String,
        amount: Double
    ): Flow<Result<Double>>

    suspend fun convertCurrencyToAll(from: String, amount: Double): Flow<Result<List<CurrencyExchange>>>
}
