package com.mn.flights.data.network.models

data class SearchFlightsResponse(
    val action: String,
    val content: Content,
    val sessionToken: String,
    val status: String
)