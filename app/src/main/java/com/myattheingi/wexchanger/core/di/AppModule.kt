package com.myattheingi.wexchanger.core.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences

import androidx.work.WorkManager
import com.myattheingi.wexchanger.core.data.local.CurrencyDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("wave_exchanger", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)


    @Provides
    @Singleton
    fun provideCurrencyDatabase(@ApplicationContext context: Context) = CurrencyDatabase.getDatabase(context)
}