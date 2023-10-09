package com.mn.flights.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mn.flights.data.network.AppResult
import com.mn.flights.data.network.models.PlaceId
import com.mn.flights.domain.repositories.AutoSuggestPlace
import com.mn.flights.domain.repositories.RoundTripFlight
import com.mn.flights.domain.repositories.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

sealed interface EventAction {
    data class ShowEmptyView(val shown: Boolean) : EventAction
    data class ShowErrorMessage(val message: String) : EventAction
}

class MainViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    private val _fromAutoSuggestPlaces = MutableStateFlow<List<AutoSuggestPlace>>(listOf())
    val fromAutoSuggestPlaces: StateFlow<List<AutoSuggestPlace>> get() = _fromAutoSuggestPlaces

    private val _toAutoSuggestPlaces = MutableStateFlow<List<AutoSuggestPlace>>(listOf())
    val toAutoSuggestPlaces: StateFlow<List<AutoSuggestPlace>> get() = _toAutoSuggestPlaces

    private val _loader = MutableLiveData<Boolean>(false)
    val loader: LiveData<Boolean> get() = _loader

    private val _eventActions = MutableLiveData<EventAction>()
    val eventActions: LiveData<EventAction> get() = _eventActions

    private val _roundTrips = MutableStateFlow<List<RoundTripFlight>>(listOf())
    val roundTrips: StateFlow<List<RoundTripFlight>> get() = _roundTrips

    val isFormValid: StateFlow<Boolean> = combine(
        _fromAutoSuggestPlaces,
        _toAutoSuggestPlaces
    ) { fromPlaces, toPlaces ->
        fromPlaces.isNotEmpty() && toPlaces.isNotEmpty() && fromPlaces != toPlaces
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    suspend fun fetchAutoSuggestPlaces(searchText: String, destinationType: DestinationType) {
        if (searchText.length > 2 && searchText.isNotBlank()) {
            searchRepository.getAutoSuggestPlaces(searchText).collect { result ->
                when (result) {
                    is AppResult.Error -> {
                        result.message?.takeIf { it.isNotBlank() }?.let {
                            _eventActions.postValue(EventAction.ShowErrorMessage(it))
                        }
                    }

                    is AppResult.Success -> {
                        when (destinationType) {
                            DestinationType.FROM -> _fromAutoSuggestPlaces.value =
                                result.successData

                            DestinationType.TO -> _toAutoSuggestPlaces.value = result.successData
                        }
                    }

                    AppResult.LOADING -> {
                        //Nothing to do
                    }
                }
            }
        } else {
            when (destinationType) {
                DestinationType.FROM -> _fromAutoSuggestPlaces.value = listOf()
                DestinationType.TO -> _toAutoSuggestPlaces.value = listOf()
            }
        }
    }

    suspend fun getRoundTrips(
        origin: String,
        destination: String
    ) {
        searchRepository.getRoundTrip(
            originName = origin.extractNameAndIata().name,
            originIata = origin.extractNameAndIata().iata,
            destinationName = destination.extractNameAndIata().name,
            destinationIata = destination.extractNameAndIata().iata
        ).collect { result ->
            when (result) {
                is AppResult.Error -> {
                    _loader.postValue(false)
                    _eventActions.postValue(EventAction.ShowErrorMessage(result.message?.takeIf { !it.isNullOrBlank() }
                        ?: "Une erreur est survenue"))
                }

                is AppResult.Success -> {
                    _loader.postValue(false)
                    _eventActions.postValue(EventAction.ShowEmptyView(result.successData.isEmpty()))
                    _roundTrips.value = result.successData
                }

                AppResult.LOADING -> {
                    _loader.postValue(true)
                }
            }
        }
    }

    private fun String.extractNameAndIata(): PlaceId {
        val matchResult = Regex("\\(([A-Z]{3})\\)").find(this)
        val name = matchResult?.let { this.replace(it.value, "") } ?: this
        val iata = matchResult?.groupValues?.get(1) ?: ""
        return PlaceId(name, iata)
    }
}

enum class DestinationType {
    FROM, TO
}
