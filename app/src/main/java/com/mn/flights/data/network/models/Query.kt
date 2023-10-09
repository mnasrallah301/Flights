package com.mn.flights.data.network.models

data class Query(
    val locale: String,
    val market: String,
    val currency: String? = null,
    val searchTerm: String? = null,
    val queryLegs: List<QueryLeg>? = null,
    val adults: Int = 1,
    val cabin_class: String = "CABIN_CLASS_ECONOMY"
)