package com.cromulent.vacationapp.presentation.gpsScreen

data class GpsState(
    val currentLocationName: String = "",
    val isLocating: Boolean = false,
    val isSearching: Boolean = false,
    val searchResults: List<String> = listOf()
)
