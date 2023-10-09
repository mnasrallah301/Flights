package com.mn.flights.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mn.flights.R
import com.mn.flights.databinding.ItemSearchFlightBinding
import com.mn.flights.domain.repositories.RoundTripFlight

class SearchFlightsAdapter(private val context: Context, private val items: List<RoundTripFlight>) :
    RecyclerView.Adapter<SearchFlightsAdapter.FlightsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightsViewHolder {
        val binding =
            ItemSearchFlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightsViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FlightsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class FlightsViewHolder(private val binding: ItemSearchFlightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RoundTripFlight) {
            binding.flightNumber.text =
                context.getString(
                    R.string.flights,
                    item.departFlight.marketingFlightNumber,
                    item.arriveFlight.marketingFlightNumber
                )
            binding.price.text = "$${item.price}"

            binding.itinerary1.stops.isVisible = item.departFlight.stopsCount != 0
            binding.itinerary1.arrival.text = item.departFlight.destinationPlaceId
            binding.itinerary1.departure.text = item.departFlight.originPlaceId
            binding.itinerary1.arrivalTimeTxt.text = item.departFlight.destinationTime
            binding.itinerary1.depTimeTxt.text = item.departFlight.originTime
            binding.itinerary1.carrierImg.load(item.departFlight.carrierImg)

            binding.itinerary2.stops.isVisible = item.arriveFlight.stopsCount != 0
            binding.itinerary2.arrival.text = item.arriveFlight.destinationPlaceId
            binding.itinerary2.departure.text = item.arriveFlight.originPlaceId
            binding.itinerary2.arrivalTimeTxt.text = item.arriveFlight.destinationTime
            binding.itinerary2.depTimeTxt.text = item.arriveFlight.originTime
            binding.itinerary2.carrierImg.load(item.arriveFlight.carrierImg)
        }
    }
}