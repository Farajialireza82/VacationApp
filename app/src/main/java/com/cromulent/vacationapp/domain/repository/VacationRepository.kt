package com.cromulent.vacationapp.domain.repository

import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface VacationRepository {

    suspend fun getNearbyLocations(
        latLng: String,
        category: String? = null
    ): Flow<Resource<List<Location?>>>


    suspend fun getLocationDetails(
        locationId: String,
        forceRefresh: Boolean = false
    ): Flow<Resource<Location?>>

    suspend fun getLocationPhotos(locationId: String?): Flow<Resource<List<LocationPhoto>?>>

}