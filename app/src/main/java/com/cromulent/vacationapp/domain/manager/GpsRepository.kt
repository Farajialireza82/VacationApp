package com.cromulent.vacationapp.domain.manager

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface GpsRepository {

    val currentCoordinates: StateFlow<CoordinatesData?>

    fun locateUser(onUserLocated:(CoordinatesData) -> Unit)

    suspend fun saveCurrentCoordinates(currentCoordinates: CoordinatesData)

    fun readCurrentCoordinates(): Flow<CoordinatesData>
}