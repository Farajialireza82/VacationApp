package com.cromulent.vacationapp.presentation.detailsScreen

import com.cromulent.vacationapp.model.Location

data class DetailsState(
    val location: Location? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
