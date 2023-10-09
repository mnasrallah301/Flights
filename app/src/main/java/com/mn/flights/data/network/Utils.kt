package com.mn.flights.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Response
import java.io.IOException

object Utils {
    fun <T : Any> handleApiError(resp: Response<T>): AppResult.Error {
        val error = ApiErrorUtils.parseError(resp)
        return AppResult.Error(Exception(error.message))
    }
}

object ApiErrorUtils {
    fun parseError(response: Response<*>): APIError {
        val gson = GsonBuilder().create()
        val error: APIError
        try {
            error = gson.fromJson(response.errorBody()?.string(), APIError::class.java)
        } catch (e: IOException) {
            e.message?.let { Log.d("ApiErrorUtils", it) }
            return APIError()
        }
        return error
    }

}

data class APIError(val message: String) {
    constructor() : this("")
}

sealed class AppResult<out T> {
    object LOADING : AppResult<Nothing>()
    data class Success<out T>(val successData: T) : AppResult<T>()
    class Error(
        private val exception: java.lang.Exception,
        val message: String? = exception.localizedMessage
    ) : AppResult<Nothing>()
}

fun noNetworkConnectivityError(): AppResult.Error =
    AppResult.Error(NetworkNotAvailableException())

class NetworkNotAvailableException(message: String = "Network connection is not available") :
    Exception(message)

interface NetworkManager {
    fun isOnline(context: Context): Boolean
}

class NetworkManagerImpl : NetworkManager {
    override fun isOnline(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.run {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false
                }
            }
        }
        return result
    }
}
