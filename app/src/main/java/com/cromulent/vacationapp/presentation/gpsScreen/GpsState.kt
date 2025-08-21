package com.cromulent.vacationapp.presentation.gpsScreen

import com.cromulent.vacationapp.model.CoordinatesData

data class GpsState(
    val currentLocationName: String = "",
    val isLocating: Boolean = false,
    val isSearching: Boolean = false,
    val searchResults: List<CoordinatesData?> = listOf()
)
