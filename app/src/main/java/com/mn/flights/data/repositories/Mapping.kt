package com.mn.flights.data.repositories

import com.mn.flights.data.network.models.Carrier
import com.mn.flights.data.network.models.Leg
import com.mn.flights.data.network.models.Place
import com.mn.flights.data.network.models.PlaceSearch
import com.mn.flights.data.network.models.SearchFlightsResponse
import com.mn.flights.data.network.models.Segment
import com.mn.flights.domain.repositories.AutoSuggestPlace
import com.mn.flights.domain.repositories.RoundTripFlight
import com.mn.flights.domain.repositories.SearchFlight

fun Place.toAutoSuggestPlace(): AutoSuggestPlace =
    AutoSuggestPlace(
        name = "$name ${if (iataCode.isNullOrEmpty()) "" else "($iataCode)"}",
        country = countryName,
        highlighting = if (highlighting.isNotEmpty()) highlighting[0] else listOf(),
        isAirport = iataCode?.isEmpty() == false
    )

fun mapLegToSearchFlight(
    legId: Leg?,
    segments: Map<String, Segment>,
    carriers: Map<String, Carrier>,
    places: Map<String, PlaceSearch>
): SearchFlight? {
    legId ?: return null
    val segment = segments[legId.segmentIds[0]] ?: return null
    return SearchFlight(
        originPlaceId = places[legId.originPlaceId]?.iata ?: "",
        destinationPlaceId = places[legId.destinationPlaceId]?.iata ?: "",
        originTime = formatTime(legId.departureDateTime.hour, legId.departureDateTime.minute),
        destinationTime = formatTime(legId.arrivalDateTime.hour, legId.arrivalDateTime.minute),
        stopsCount = legId.stopCount,
        carrierImg = carriers[segment.marketingCarrierId]?.imageUrl ?: "",
        marketingFlightNumber = segment.marketingFlightNumber
    )
}

fun SearchFlightsResponse.toSearchFlights(): List<RoundTripFlight> =
    this.content.results.itineraries.entries.mapNotNull { itinerary ->
        val price = itinerary.value.pricingOptions.firstOrNull()?.price?.amount

        if (itinerary.value.legIds.size != 2) return@mapNotNull null

        val legId1 = this.content.results.legs[itinerary.value.legIds[0]]
        val legId2 = this.content.results.legs[itinerary.value.legIds[1]]

        val departFlight = mapLegToSearchFlight(
            legId1,
            this.content.results.segments,
            this.content.results.carriers,
            this.content.results.places
        )
        val arriveFlight = mapLegToSearchFlight(
            legId2,
            this.content.results.segments,
            this.content.results.carriers,
            this.content.results.places
        )

        if (departFlight != null && arriveFlight != null) {
            RoundTripFlight(
                price = "${price?.toDouble()?.div(1000)}",
                departFlight = departFlight,
                arriveFlight = arriveFlight,
            )
        } else null
    }

fun formatTime(hour: Int, minute: Int): String {
    return String.format("%02d:%02d", hour, minute)
}