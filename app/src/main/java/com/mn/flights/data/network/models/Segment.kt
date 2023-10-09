package com.mn.flights.data.network.models

data class Segment(
    val arrivalDateTime: ArrivalDateTime,
    val departureDateTime: DepartureDateTime,
    val destinationPlaceId: String,
    val durationInMinutes: Int,
    val marketingCarrierId: String,
    val marketingFlightNumber: String,
    val operatingCarrierId: String,
    val originPlaceId: String
)