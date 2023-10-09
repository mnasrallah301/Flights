package com.mn.flights.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.mn.flights.databinding.ActivityMainBinding
import com.mn.flights.domain.repositories.AutoSuggestPlace
import com.mn.flights.domain.repositories.RoundTripFlight
import com.mn.flights.presentation.adapters.SearchAutocompleteAdapter
import com.mn.flights.presentation.adapters.SearchFlightsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)

        val fromAutoCompleteItems = mutableListOf<AutoSuggestPlace>()
        val fromAdapter = SearchAutocompleteAdapter(this, fromAutoCompleteItems)
        (binding.from.editText as? AutoCompleteTextView)?.setAdapter(fromAdapter)

        (binding.from.editText as? AutoCompleteTextView)?.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                mainViewModel.fetchAutoSuggestPlaces(
                    searchText = text.toString(),
                    DestinationType.FROM
                )
            }
        }
        lifecycleScope.launch {
            mainViewModel.fromAutoSuggestPlaces.collectLatest {
                fromAutoCompleteItems.clear()
                fromAutoCompleteItems.addAll(it)
                fromAdapter.notifyDataSetChanged()
            }
        }

        val toAutoCompleteItems = mutableListOf<AutoSuggestPlace>()
        val toAdapter = SearchAutocompleteAdapter(this, toAutoCompleteItems)
        (binding.to.editText as? AutoCompleteTextView)?.setAdapter(toAdapter)

        (binding.to.editText as? AutoCompleteTextView)?.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launch(Dispatchers.IO) {
                mainViewModel.fetchAutoSuggestPlaces(
                    searchText = text.toString(),
                    DestinationType.TO
                )
            }
        }
        lifecycleScope.launch {
            mainViewModel.toAutoSuggestPlaces.collectLatest {
                toAutoCompleteItems.clear()
                toAutoCompleteItems.addAll(it)
                toAdapter.notifyDataSetChanged()
            }
        }

        val flights = mutableListOf<RoundTripFlight>()
        val flightsAdapter = SearchFlightsAdapter(this, flights)
        binding.flightsRecyclerView.adapter = flightsAdapter

        lifecycleScope.launch {
            mainViewModel.isFormValid.collect {
                binding.confirmButton.isEnabled = it
            }
        }
        binding.confirmButton.setOnClickListener {
            binding.from.editText?.clearFocus()
            binding.to.editText?.clearFocus()
            it.hideKeyboard()
            lifecycleScope.launch {
                mainViewModel.getRoundTrips(
                    origin = binding.from.editText?.text.toString(),
                    destination = binding.to.editText?.text.toString()
                )
            }
        }

        lifecycleScope.launch {
            mainViewModel.roundTrips.collectLatest {
                flights.clear()
                flights.addAll(it)
                flightsAdapter.notifyDataSetChanged()
            }
        }

        mainViewModel.eventActions.observe(this) { eventAction ->
            when (eventAction) {
                is EventAction.ShowEmptyView -> {
                    binding.empty.root.isVisible = eventAction.shown
                }

                is EventAction.ShowErrorMessage -> {
                    binding.empty.root.isVisible = false
                    flights.clear()
                    flightsAdapter.notifyDataSetChanged()
                    Snackbar.make(binding.root, eventAction.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        mainViewModel.loader.observe(this) {
            binding.empty.root.isVisible = false
            binding.flightsRecyclerView.isVisible = !it
            binding.shimmerViewContainer.isVisible = it
        }
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
