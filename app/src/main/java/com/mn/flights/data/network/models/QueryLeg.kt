package com.mn.flights.data.network.models

data class QueryLeg(
    val date: Date,
    val destination_place_id: PlaceId,
    val origin_place_id: PlaceId
)