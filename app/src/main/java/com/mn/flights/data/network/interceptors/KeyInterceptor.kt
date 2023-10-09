package com.mn.flights.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class KeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-api-key", "sh428739766321522266746152871799") // This is a public key for tests purposes, don't do this in production environment
            .build()
        return chain.proceed(request)
    }
}