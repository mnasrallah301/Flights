package com.mn.flights.data.network.models

data class Carrier(
    val allianceId: String,
    val displayCode: String,
    val iata: String,
    val icao: String,
    val imageUrl: String,
    val name: String
)