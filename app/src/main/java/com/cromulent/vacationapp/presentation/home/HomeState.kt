package com.cromulent.vacationapp.presentation.home

import com.cromulent.vacationapp.model.Location

data class HomeState(
    val locations: List<Location?> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
