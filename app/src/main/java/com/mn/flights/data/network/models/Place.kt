package com.mn.flights.data.network.models

data class Place(
    val cityName: String,
    val countryId: String,
    val countryName: String,
    val entityId: String,
    val hierarchy: String,
    val highlighting: List<List<Int>>,
    val iataCode: String? = null,
    val location: String,
    val name: String,
    val parentId: String,
    val type: String
)