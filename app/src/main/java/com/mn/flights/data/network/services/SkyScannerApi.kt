package com.mn.flights.data.network.services

import com.mn.flights.data.network.models.AutoSuggestFlightsResponse
import com.mn.flights.data.network.models.QueryWrapped
import com.mn.flights.data.network.models.SearchFlightsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SkyScannerApi {
    @Headers("Content-Type: application/json")
    @POST("v3/autosuggest/flights")
    suspend fun getAutoSuggestPlace(@Body query: QueryWrapped): Response<AutoSuggestFlightsResponse>

    @Headers("Content-Type: application/json")
    @POST("v3/flights/live/search/create")
    suspend fun getRoundTrip(@Body query: QueryWrapped): Response<SearchFlightsResponse>
}