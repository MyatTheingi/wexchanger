package com.myattheingi.wexchanger.core.di

import com.myattheingi.wexchanger.core.data.local.CurrencyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideLiveRatesDao(database: CurrencyDatabase) = database.liveRatesDao()

    @Provides
    @Singleton
    fun provideCurrencyListDao(database: CurrencyDatabase) = database.currencyListDao()
}