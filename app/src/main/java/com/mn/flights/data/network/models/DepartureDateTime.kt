package com.mn.flights.data.network.models

data class DepartureDateTime(
    val day: Int,
    val hour: Int,
    val minute: Int,
    val month: Int,
    val second: Int,
    val year: Int
)