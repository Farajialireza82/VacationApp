package com.cromulent.vacationapp.domain.repository

import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import kotlinx.coroutines.flow.Flow

interface OpenWeatherMapRepository {

    suspend fun searchForCoordinatesData(
        query: String,
        limit: Int = 10
    ): Flow<List<CoordinatesData?>>

}