package com.myattheingi.wexchanger.core.di

import com.myattheingi.wexchanger.core.data.remote.CurrencyApiService
import com.myattheingi.wexchanger.BuildConfig

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    companion object {
        private fun customOkHttpClientBuilder(): OkHttpClient.Builder {
            Credentials
            return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .apply {
                    if (BuildConfig.DEBUG) {
                        val logging = HttpLoggingInterceptor()
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                        addInterceptor(logging)
                    }
                }
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                    val response = chain.proceed(requestBuilder.build())
                    response
                }
        }

        @Provides
        @Singleton
        fun provideCurrencyApiService(): CurrencyApiService {
            val client = customOkHttpClientBuilder()
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(CurrencyApiService::class.java)
        }
    }
}
