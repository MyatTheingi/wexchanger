package com.myattheingi.wexchanger.core.data.local.datasource

import com.myattheingi.wexchanger.core.data.local.dao.CurrencyListDao
import com.myattheingi.wexchanger.core.data.local.dao.LiveRatesDao
import com.myattheingi.wexchanger.core.data.local.models.CurrencyListEntity
import com.myattheingi.wexchanger.core.data.local.models.LiveRateEntity
import javax.inject.Inject

interface CurrencyLocalDataSource {
    // Methods for managing live rates
    suspend fun storeLiveRates(entity: LiveRateEntity)
    suspend fun getLiveRates(): LiveRateEntity?
    suspend fun deleteAllLiveRates()

    // Methods for managing currency lists
    suspend fun storeCurrencyList(entity: CurrencyListEntity)
    suspend fun getCurrencyList(): CurrencyListEntity?
}

class CurrencyLocalDataSourceImpl @Inject constructor(
    private val liveRatesDao: LiveRatesDao,
    private val currencyListDao: CurrencyListDao
) : CurrencyLocalDataSource {
    override suspend fun storeLiveRates(entity: LiveRateEntity) {
        liveRatesDao.insertLiveRates(entity)
    }

    override suspend fun getLiveRates(): LiveRateEntity? {
        return liveRatesDao.getLiveRates()
    }

    override suspend fun deleteAllLiveRates() {
         liveRatesDao.deleteAllLiveRates()
    }

    override suspend fun storeCurrencyList(entity: CurrencyListEntity) {
        currencyListDao.insertCurrencyList(entity)
    }

    override suspend fun getCurrencyList(): CurrencyListEntity? {
        return currencyListDao.getCurrencyList()
    }
}
