package com.cromulent.vacationapp.presentation.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val vacationRepository: VacationRepository,
    val gpsRepository: GpsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    val currentCoordinates = gpsRepository.currentCoordinates

    private var cachedLocationData: Map<String, List<Location?>> = mutableMapOf()

    fun clearCachedLocations() {
        cachedLocationData = mutableMapOf()
    }


    fun getNearbyLocations(
        category: String
    ) {

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        if (cachedLocationData.contains(category)) {
            _state.value =
                _state.value.copy(locations = cachedLocationData[category]!!, isLoading = false)
            return
        }


        viewModelScope.launch {
            vacationRepository
                .getNearbyLocations(
                    latLng = currentCoordinates.value?.getCoordinatesString().toString(),
                    category = category
                ).collectLatest { locationsResource ->

                    when (locationsResource) {
                        is Resource.Error<*> -> {

                            _state.value = _state.value.copy(
                                error = locationsResource.message,
                                isLoading = false
                            )

                        }

                        is Resource.Success<*> -> {
                            var locations = locationsResource.data ?: listOf()

                            locations.forEach { location ->

                                vacationRepository
                                    .getLocationPhotos(location?.locationId)
                                    .collectLatest { photoResource ->

                                        when (photoResource) {
                                            is Resource.Error<*> -> {
                                                _state.value = _state.value.copy(
                                                    error = photoResource.message
                                                )
                                            }

                                            is Resource.Success<*> -> {
                                                val index = locations.indexOf(location)
                                                locations[index]?.locationPhotos = photoResource.data

                                            }
                                        }

                                    }
                            }

                            locations =
                                locations.sortedBy { it?.locationPhotos?.isEmpty() }


                            cachedLocationData =
                                cachedLocationData + (category to locations)

                            _state.value = _state.value.copy(
                                locations = locations,
                                isLoading = false
                            )
                        }
                    }
                    _state.value = _state.value.copy(isLoading = false)


                }

        }
    }

}