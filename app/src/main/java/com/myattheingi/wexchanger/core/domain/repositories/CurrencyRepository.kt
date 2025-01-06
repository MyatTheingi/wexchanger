package com.myattheingi.wexchanger.core.domain.repositories

import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo
import com.myattheingi.wexchanger.core.domain.models.CurrencyQuotes
import kotlinx.coroutines.flow.Flow


interface CurrencyRepository {

    // Cache live rates from the remote source
    suspend fun fetchAndStoreLiveRates()

    // Fetch live rates and cache them if necessary
    suspend fun getLiveRates(): Flow<Result<CurrencyQuotes>>

    // Fetch the currency list and store it locally
    suspend fun fetchAndStoreCurrencyList()

    // Get the stored currency list
    suspend fun getCurrencyList(): Flow<Result<List<CurrencyInfo>>>

    // Convert currency
    suspend fun convertCurrency(from: String, to: String, amount: Double): Flow<Result<Double>>

}
