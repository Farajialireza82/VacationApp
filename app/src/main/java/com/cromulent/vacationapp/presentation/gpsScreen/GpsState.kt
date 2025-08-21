package com.cromulent.vacationapp.presentation.gpsScreen

import com.cromulent.vacationapp.model.CoordinatesData

data class GpsState(
    val isLocating: Boolean = false,
    val isSearching: Boolean = false,
    val isCoordinateSelected: Boolean = false,
    val error: String? = null,
    val searchResults: List<CoordinatesData?> = listOf()
)
