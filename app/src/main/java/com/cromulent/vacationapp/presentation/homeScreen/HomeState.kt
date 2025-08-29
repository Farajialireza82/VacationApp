package com.cromulent.vacationapp.presentation.homeScreen

import com.cromulent.vacationapp.model.Location

data class HomeState(
    val popularLocations: List<Location?> = emptyList(),
    val recommendedLocations: List<Location?> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
