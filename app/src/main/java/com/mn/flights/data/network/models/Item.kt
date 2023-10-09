package com.mn.flights.data.network.models

data class Item(
    val agentId: String,
    val deepLink: String,
    val fares: List<Fare>,
    val price: Price
)