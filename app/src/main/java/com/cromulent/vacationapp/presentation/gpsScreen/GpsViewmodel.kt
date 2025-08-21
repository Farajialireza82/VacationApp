package com.cromulent.vacationapp.presentation.gpsScreen

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.repository.OpenWeatherMapRepository
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GpsViewmodel @Inject constructor(
    val gpsRepository: GpsRepository,
    val openWeatherMapRepository: OpenWeatherMapRepository
) : ViewModel() {

    private val _state = MutableStateFlow<GpsState>(GpsState())
    val state: StateFlow<GpsState> = _state.asStateFlow()

    val currentCoordinates = gpsRepository.currentCoordinates


    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        _state.value = _state.value.copy(
            isLocating = true
        )
        gpsRepository.locateUser {
            _state.value = _state.value.copy(
                isLocating = false
            )
            _state.value = _state.value.copy(
                isCoordinateSelected = true
            )
            setCurrentCoordinates(it)
        }
    }

    fun setCurrentCoordinates(data: CoordinatesData) {
        viewModelScope.launch {
            gpsRepository.saveCurrentCoordinates(data)
            _state.value = _state.value.copy(
                isCoordinateSelected = true
            )
        }
    }

    fun search(query: String) {
        _state.value = _state.value.copy(
            isSearching = true
        )
        viewModelScope.launch {
            openWeatherMapRepository
                .searchForCoordinatesData(query)
                .collect {

                    when (it) {
                        is Resource.Error<*> -> {
                            _state.value = _state.value.copy(
                                isSearching = false,
                                error = it.message
                            )
                        }

                        is Resource.Success<List<CoordinatesData>> -> {

                            if (it.data?.isEmpty() == true) {

                                _state.value = _state.value.copy(
                                    isSearching = false,
                                    error = "No Results Found"
                                )

                            } else {
                                _state.value = _state.value.copy(
                                    isSearching = false,
                                    searchResults = it.data ?: listOf()
                                )
                            }
                        }
                    }


                }
        }

    }
}