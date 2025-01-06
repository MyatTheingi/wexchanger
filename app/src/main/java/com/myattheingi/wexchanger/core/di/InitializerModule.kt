package com.myattheingi.wexchanger.core.di


import android.content.Context
import com.myattheingi.wexchanger.android.WorkManagerInitializer
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent


@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerModule {

    companion object {
        //a helper method to resolve the InitializerEntryPoint from the context
        fun resolve(context: Context): InitializerModule {
            val appContext = context.applicationContext ?: throw IllegalStateException()
            return EntryPointAccessors.fromApplication(
                appContext,
                InitializerModule::class.java
            )
        }
    }

    fun inject(initializer: WorkManagerInitializer)
}