package com.mn.flights.data.repositories

import android.content.Context
import com.mn.flights.data.network.AppResult
import com.mn.flights.data.network.NetworkManager
import com.mn.flights.data.network.Utils.handleApiError
import com.mn.flights.data.network.models.Date
import com.mn.flights.data.network.models.PlaceId
import com.mn.flights.data.network.models.Query
import com.mn.flights.data.network.models.QueryLeg
import com.mn.flights.data.network.models.QueryWrapped
import com.mn.flights.data.network.noNetworkConnectivityError
import com.mn.flights.data.network.services.SkyScannerApi
import com.mn.flights.domain.repositories.AutoSuggestPlace
import com.mn.flights.domain.repositories.RoundTripFlight
import com.mn.flights.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val context: Context,
    private val skyScannerApi: SkyScannerApi,
    private val networkManager: NetworkManager
) : SearchRepository {
    override suspend fun getAutoSuggestPlaces(searchText: String): Flow<AppResult<List<AutoSuggestPlace>>> {
        return flow {
            if (networkManager.isOnline(context)) {
                try {
                    emit(AppResult.LOADING)
                    val response = skyScannerApi.getAutoSuggestPlace(
                        QueryWrapped(
                            Query(
                                locale = "en-GB",
                                market = "UK",
                                searchTerm = searchText
                            )
                        )
                    )
                    if (response.isSuccessful && response.body() != null) {
                        emit(AppResult.Success(response.body()!!.places.map { it.toAutoSuggestPlace() }))
                    } else {
                        emit(handleApiError(response))
                    }
                } catch (e: Exception) {
                    emit(AppResult.Error(e))
                }
            } else {
                emit(noNetworkConnectivityError())
            }
        }
    }

    override suspend fun getRoundTrip(
        originName: String,
        originIata: String?,
        destinationName: String,
        destinationIata: String?,
    ): Flow<AppResult<List<RoundTripFlight>>> {
        return flow {
            if (networkManager.isOnline(context)) {
                try {
                    emit(AppResult.LOADING)
                    val response = skyScannerApi.getRoundTrip(
                        query = QueryWrapped(
                            Query(
                                locale = "en-GB",
                                market = "US",
                                currency = "USD",
                                queryLegs = listOf(
                                    QueryLeg(
                                        date = Date(day = 15, month = 10, year = 2023),
                                        origin_place_id = PlaceId(
                                            name = originName,
                                            iata = originIata ?: ""
                                        ),
                                        destination_place_id = PlaceId(
                                            name = destinationName,
                                            iata = destinationIata ?: ""
                                        ),
                                    ),
                                    QueryLeg(
                                        date = Date(day = 29, month = 10, year = 2023),
                                        origin_place_id = PlaceId(
                                            name = destinationName,
                                            iata = destinationIata ?: ""
                                        ),
                                        destination_place_id = PlaceId(
                                            name = originName,
                                            iata = originIata ?: ""
                                        ),
                                    ),
                                )
                            )
                        )
                    )
                    if (response.isSuccessful && response.body() != null) {
                        emit(AppResult.Success(response.body()!!.toSearchFlights()))
                    } else {
                        emit(handleApiError(response))
                    }
                } catch (e: Exception) {
                    emit(AppResult.Error(e))
                }
            } else {
                emit(noNetworkConnectivityError())
            }
        }
    }
}
