package com.cromulent.vacationapp.presentation.searchScreen

import com.cromulent.vacationapp.model.Location

data class SearchState(
    val data: List<Location>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
