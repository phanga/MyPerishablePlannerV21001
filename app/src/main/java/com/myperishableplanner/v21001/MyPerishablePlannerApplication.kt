package com.myperishableplanner.v21001

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class MyPerishablePlannerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin{
           androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MyPerishablePlannerApplication)
            modules(appModule)
        }

    }
}