package com.mn.flights.domain.repositories

import com.mn.flights.data.network.AppResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getAutoSuggestPlaces(searchText: String): Flow<AppResult<List<AutoSuggestPlace>>>
    suspend fun getRoundTrip(
        originName: String,
        originIata: String?,
        destinationName: String,
        destinationIata: String?,
    ): Flow<AppResult<List<RoundTripFlight>>>
}

data class AutoSuggestPlace(
    val name: String,
    val country: String,
    val highlighting: List<Int>,
    val isAirport: Boolean
) {
    override fun toString(): String {
        return name
    }
}

data class SearchFlight(
    val carrierImg: String,
    val originPlaceId: String,
    val destinationPlaceId: String,
    val originTime: String,
    val destinationTime: String,
    val stopsCount: Int,
    val marketingFlightNumber: String,
)

data class RoundTripFlight(
    val departFlight: SearchFlight,
    val arriveFlight: SearchFlight,
    val price: String,
)