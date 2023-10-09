package com.mn.flights.data.network.models

data class Leg(
    val arrivalDateTime: ArrivalDateTime,
    val departureDateTime: DepartureDateTime,
    val destinationPlaceId: String,
    val durationInMinutes: Int,
    val marketingCarrierIds: List<String>,
    val operatingCarrierIds: List<String>,
    val originPlaceId: String,
    val segmentIds: List<String>,
    val stopCount: Int
)