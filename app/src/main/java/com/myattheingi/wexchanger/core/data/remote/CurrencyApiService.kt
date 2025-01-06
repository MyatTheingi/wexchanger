package com.myattheingi.wexchanger.core.data.remote

import com.myattheingi.wexchanger.BuildConfig
import com.myattheingi.wexchanger.core.data.remote.models.ConversionResponse
import com.myattheingi.wexchanger.core.data.remote.models.CurrencyListResponse
import com.myattheingi.wexchanger.core.data.remote.models.LiveRatesResponse
import retrofit2.http.*

interface CurrencyApiService {
    // Fetch live exchange rates
    @GET("live")
    suspend fun getLiveRates(
        @Query("access_key") accessKey: String = BuildConfig.ACCESS_KEY
    ): LiveRatesResponse

    // Fetch list of available currencies
    @GET("list")
    suspend fun getCurrencyList(
        @Query("access_key") accessKey: String = BuildConfig.ACCESS_KEY
    ): CurrencyListResponse

    // Convert currency
    @GET("convert")
    suspend fun convertCurrency(
        @Query("access_key") accessKey: String = BuildConfig.ACCESS_KEY,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): ConversionResponse
}
