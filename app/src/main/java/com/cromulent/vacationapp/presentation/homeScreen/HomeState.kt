package com.cromulent.vacationapp.presentation.homeScreen

import com.cromulent.vacationapp.model.Location

data class HomeState(
    val popularLocations: Map<String, List<Location?>> = mutableMapOf(),
    val recommendedLocations: Map<String, List<Location?>> = mutableMapOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)
