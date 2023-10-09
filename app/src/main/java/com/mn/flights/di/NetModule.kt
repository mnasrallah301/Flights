package com.mn.flights.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mn.flights.data.network.NetworkManager
import com.mn.flights.data.network.NetworkManagerImpl
import com.mn.flights.data.network.interceptors.KeyInterceptor
import com.mn.flights.data.network.services.SkyScannerApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BODY_LoggingInterceptor = "BODY"
private const val BASIC_LoggingInterceptor = "BASIC"

private val servicesModule = module {
    single { get<Retrofit>().create(SkyScannerApi::class.java) as SkyScannerApi }
    single<NetworkManager> { NetworkManagerImpl() }
}
private val libHttpCallsModule = module {

    //Interceptors
    single<KeyInterceptor> { KeyInterceptor() }

    single<HttpLoggingInterceptor>(named(BODY_LoggingInterceptor)) {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single<HttpLoggingInterceptor>(named(BASIC_LoggingInterceptor)) {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    //OkHttpClients
    single<OkHttpClient> {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(get<HttpLoggingInterceptor>(named(BODY_LoggingInterceptor)))
            }
            addInterceptor(get<KeyInterceptor>())
        }.build()
    }

    //Gson
    single {
        GsonBuilder()
            .serializeNulls()
            .create() as Gson
    }
    single { GsonConverterFactory.create(get()) as Converter.Factory }

    //Retrofits
    single {
        Retrofit.Builder()
            .baseUrl("https://partners.api.skyscanner.net/apiservices/")
            .client(get())
            .addConverterFactory(get())
            .build() as Retrofit
    }
}

val netModule = listOf<Module>(servicesModule, libHttpCallsModule)