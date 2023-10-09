package com.mn.flights.presentation

import android.app.Application
import com.mn.flights.di.netModule
import com.mn.flights.di.repositoriesModule
import com.mn.flights.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FlightsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FlightsApplication)
            modules(netModule + repositoriesModule + viewModelsModule)
        }
    }
}