package com.cromulent.vacationapp.domain.repository

import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import kotlinx.coroutines.flow.Flow

interface VacationRepository {

    suspend fun getNearbyLocations(
        latLng: String,
        category: String? = null
    ): Flow<List<Location?>>

    suspend fun getLocationPhotos(locationId: String?): Flow<List<LocationPhoto>?>

}