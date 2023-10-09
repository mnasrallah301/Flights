package com.mn.flights.data.network.models

data class PricingOption(
    val agentIds: List<String>,
    val id: String,
    val items: List<Item>,
    val price: Price,
    val pricingOptionFare: Any,
    val transferType: String
)