package com.cromulent.vacationapp.presentation.detailsScreen

import com.cromulent.vacationapp.model.Location

data class DetailsState(
    val location: Location? = Location(
        locationId = "null",
        name = "null"
    ),
    val isLoading: Boolean = false,
    val error: String? = null
)
