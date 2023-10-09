package com.mn.flights.data.network.models

data class PlaceSearch(
    val coordinates: Any,
    val entityId: String,
    val iata: String,
    val name: String,
    val parentId: String,
    val type: String
)