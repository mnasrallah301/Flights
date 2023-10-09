package com.mn.flights.data.network.models

data class SortingOptions(
    val best: List<Best>,
    val cheapest: List<Cheapest>,
    val fastest: List<Fastest>
)