package com.cromulent.vacationapp.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val vacationRepository: VacationRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private var cachedLocationData: Map<String, List<Location?>> = mutableMapOf()

    init {
        getNearbyLocations(
            latLng = "42.3455,-71.10767",
            category = "hotels"
        )
    }


    fun getNearbyLocations(
        latLng: String,
        category: String,
    ) {

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        if (cachedLocationData.contains(category)) {
            _state.value = _state.value.copy(locations = cachedLocationData[category]!!, isLoading = false)
            return
        }


        var locations: List<Location?> = listOf()
        viewModelScope.launch {
            vacationRepository
                .getNearbyLocations(
                    latLng = latLng,
                    category = category
                ).collectLatest {
                    locations = it
                }
            locations.forEach { location ->

                vacationRepository
                    .getLocationPhotos(location?.locationId).collectLatest { photos ->
                        val index = locations.indexOf(location)
                        locations[index]?.locationPhotos = photos
                    }
            }

            locations = locations.sortedBy { it?.locationPhotos?.isEmpty() }
            cachedLocationData = cachedLocationData + (category to locations)
            _state.value = _state.value.copy(locations = locations, isLoading = false)
        }
    }

}