package com.mn.flights.data.network.models

data class Results(
    val itineraries: Map<String, Itinerary>,
    val legs: Map<String, Leg>,
    val places: Map<String, PlaceSearch>,
    val segments: Map<String, Segment>,
    val carriers: Map<String, Carrier>
)