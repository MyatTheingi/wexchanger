package com.myattheingi.wexchanger.core.data.repositories

import com.myattheingi.wexchanger.core.data.local.datasource.CurrencyLocalDataSource
import com.myattheingi.wexchanger.core.data.local.models.CurrencyListEntity
import com.myattheingi.wexchanger.core.data.local.models.LiveRateEntity
import com.myattheingi.wexchanger.core.data.remote.datasource.CurrencyRemoteDataSource
import com.myattheingi.wexchanger.core.di.IoDispatcher
import com.myattheingi.wexchanger.core.domain.models.CurrencyExchange
import com.myattheingi.wexchanger.core.domain.models.CurrencyInfo
import com.myattheingi.wexchanger.core.domain.models.CurrencyRate
import com.myattheingi.wexchanger.core.domain.models.asDomainModel
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

    override suspend fun getLiveRates(): Flow<Result<List<CurrencyRate>>> {
        return flow {
            try {
                // Try fetching from local first
                val cachedLiveRates = local.getLiveRates()
                if (cachedLiveRates != null && System.currentTimeMillis() - cachedLiveRates.timestamp < 30 * 60 * 1000) {
                    // Use cached data if it is less than 30 minutes old
                    val quotes = cachedLiveRates.rates.asDomainModel()
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
                    val quotes = remoteLiveRates.asDomainModel()
                    emit(Result.success(quotes))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.catch { e ->
            emit(Result.failure(e))
        }.flowOn(dispatcher)
    }

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

    override suspend fun convertCurrencyToAll(
        from: String,
        amount: Double
    ): Flow<Result<List<CurrencyExchange>>> {
        return flow {
            try {
                val liveRates = local.getLiveRates()?.rates ?: emptyMap()
                val conversionResults = mutableMapOf<String, Pair<Double, Double>>()

                val toCurrencies = liveRates.keys
                    .map { it.takeLast(3) }
                    .distinct()
                    .filter { it != from } // Remove the 'from' currency from the list

                toCurrencies.forEach { to ->
                    val conversionResult = convertAmount(from, to, amount, liveRates)
                    val unitConversionResult = convertAmount(from, to, 1.0, liveRates)

                    if (conversionResult.isNaN() || conversionResult.isInfinite()) {
                        conversionResults[to] = Pair(Double.NaN, Double.NaN)
                    } else {
                        conversionResults[to] = Pair(unitConversionResult, conversionResult)
                    }
                }


                val quotes = conversionResults.asDomainModel()
                emit(Result.success(quotes))

            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun convertCurrencyToSingle(
        from: String,
        to: String,
        amount: Double
    ): Flow<Result<Double>> {
        return flow {
            try {
                val liveRates = local.getLiveRates()?.rates ?: emptyMap()
                val conversionResult = convertAmount(from, to, amount, liveRates)

                if (conversionResult.isNaN() || conversionResult.isInfinite()) {
                    emit(Result.failure(IllegalArgumentException("Invalid conversion rate or amount")))
                } else {
                    emit(Result.success(conversionResult))
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.flowOn(dispatcher)
    }


    private fun convertAmount(
        from: String,
        to: String,
        amount: Double,
        liveRates: Map<String, Double>
    ): Double {
        return when {
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
    }
}
