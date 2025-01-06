package com.myattheingi.wexchanger.android

import android.content.Context
import androidx.startup.Initializer
import com.myattheingi.wexchanger.core.di.InitializerModule

class DependencyGraphInitializer : Initializer<Unit> {

    override fun create(context: Context): Unit {
        //this will lazily initialize ApplicationComponent before Application's `onCreate`
        InitializerModule.resolve(context)
        return Unit
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}