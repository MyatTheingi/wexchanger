package com.myattheingi.wexchanger.core.di

import com.myattheingi.wexchanger.core.data.local.datasource.CurrencyLocalDataSource
import com.myattheingi.wexchanger.core.data.local.datasource.CurrencyLocalDataSourceImpl
import com.myattheingi.wexchanger.core.data.remote.datasource.CurrencyRemoteDataSource
import com.myattheingi.wexchanger.core.data.remote.datasource.CurrencyRemoteDataSourceImpl
import com.myattheingi.wexchanger.core.data.repositories.CurrencyRepositoryImpl
import com.myattheingi.wexchanger.core.domain.repositories.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyModule {
    @Binds
    fun repo(repo: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    fun local(dataSource: CurrencyLocalDataSourceImpl): CurrencyLocalDataSource

    @Binds
    fun remote(dataSource: CurrencyRemoteDataSourceImpl): CurrencyRemoteDataSource
}