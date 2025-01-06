package com.myattheingi.wexchanger.core.data.repositories

import com.myattheingi.wexchanger.core.data.local.datasource.CurrencyLocalDataSource
import com.myattheingi.wexchanger.core.data.local.models.CurrencyListEntity
import com.myattheingi.wexchanger.core.data.local.models.LiveRateEntity
import com.myattheingi.wexchanger.core.data.remote.datasource.CurrencyRemoteDataSource
import com.myattheingi.wexchanger.core.di.IoDispatcher
import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo
import com.myattheingi.wexchanger.core.domain.models.CurrencyQuotes
import com.myattheingi.wexchanger.core.domain.models.asDomainModel
import com.myattheingi.wexchanger.core.domain.models.toCurrencyQuotes
import com.myattheingi.wexchanger.core.domain.repositories.CurrencyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val remote: CurrencyRemoteDataSource,
    private val local: CurrencyLocalDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : CurrencyRepository {

    // Cache Live Rates from Remote to Local (Room)
    override suspend fun fetchAndStoreLiveRates() {
        withContext(dispatcher) {
            try {
                val liveRates = remote.getLiveRates()
                local.storeLiveRates(
                    LiveRateEntity(
                        rates = liveRates,
                        timestamp = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    // Get Live Rates, fallback to local if necessary
    override suspend fun getLiveRates(): Flow<Result<CurrencyQuotes>> {
        return flow {
            try {
                // Try fetching from local first
                val cachedLiveRates = local.getLiveRates()
                if (cachedLiveRates != null && System.currentTimeMillis() - cachedLiveRates.timestamp < 30 * 60 * 1000) {
                    // Use cached data if it is less than 30 minutes old
                    val quotes = cachedLiveRates.rates.toCurrencyQuotes()
                    emit(Result.success(quotes))
                } else {
                    // Otherwise, fetch from remote
                    val remoteLiveRates = remote.getLiveRates()
                    local.storeLiveRates(
                        LiveRateEntity(
                            rates = remoteLiveRates,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    val quotes = remoteLiveRates.toCurrencyQuotes()
                    emit(Result.success(quotes))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.catch { e ->
            emit(Result.failure(e))
        }.flowOn(dispatcher)
    }

    // Get Currency List from Remote to Local (Room)
    override suspend fun fetchAndStoreCurrencyList() {
        withContext(dispatcher) {
            try {
                val currencyList = remote.getCurrencyList()
                local.storeCurrencyList(CurrencyListEntity(currencies = currencyList))
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    // Get Currency List, fallback to local if available
    override suspend fun getCurrencyList(): Flow<Result<List<CurrencyInfo>>> {
        return flow {
            val cachedCurrencyList = local.getCurrencyList()
            if (cachedCurrencyList != null) {
                val currencies = cachedCurrencyList.currencies.asDomainModel()
                emit(Result.success(currencies))
            } else {
                val response = remote.getCurrencyList()
                local.storeCurrencyList(CurrencyListEntity(currencies = response))

                val quotes = response.asDomainModel()
                emit(Result.success(quotes))
            }
        }.catch { e ->
            emit(Result.failure(e))
        }.flowOn(dispatcher)
    }

    // Convert Currency (using remote data)
    override suspend fun convertCurrency(
        from: String,
        to: String,
        amount: Double
    ): Flow<Result<Double>> {

        return flow {
            try {
                val liveRates = local.getLiveRates()?.rates ?: emptyMap()
                val conversionResult = when {
                    from == "USD" -> {
                        val conversionRate = liveRates["USD$to"] ?: 0.0
                        amount * conversionRate
                    }

                    to == "USD" -> {
                        val conversionRate = liveRates["USD$from"] ?: 0.0
                        amount / conversionRate
                    }

                    else -> {
                        val usdToTarget = liveRates["USD$to"] ?: 0.0
                        val usdToBase = liveRates["USD$from"] ?: 0.0
                        amount * (usdToTarget / usdToBase)
                    }
                }

                if (conversionResult.isNaN() || conversionResult.isInfinite()) {
                    emit(Result.failure<Double>(IllegalArgumentException("Invalid conversion rate or amount")))
                } else {
                    emit(Result.success(conversionResult))
                }
            } catch (e: Exception) {
                emit(Result.failure<Double>(e))
            }
        }.flowOn(dispatcher)
    }


//    override suspend fun convertCurrency(from: String, to: String, amount: Double): Flow<Result<Double>> {
//        return withContext(dispatcher) {
//            val liveRates = local.getLiveRates()?.rates ?: emptyMap()
//            when {
//                from == "USD" -> {
//                    val conversionRate = liveRates["USD$to"] ?: 0.0
//                    amount * conversionRate
//                }
//
//                to == "USD" -> {
//                    val conversionRate = liveRates["USD$from"] ?: 0.0
//                    amount / conversionRate
//                }
//
//                else -> {
//                    val usdToTarget = liveRates["USD$to"] ?: 0.0
//                    val usdToBase = liveRates["USD$from"] ?: 0.0
//                    amount * (usdToTarget / usdToBase)
//                }
//            }
//        }
//    }
}
