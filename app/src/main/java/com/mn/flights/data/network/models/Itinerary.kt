package com.mn.flights.data.network.models

data class Itinerary(
    val legIds: List<String>,
    val pricingOptions: List<PricingOption>,
    val sustainabilityData: Any
)