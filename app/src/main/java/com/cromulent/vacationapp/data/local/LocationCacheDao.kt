package com.cromulent.vacationapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cromulent.vacationapp.model.Location

@Dao
interface LocationCacheDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun cacheLocation(location: Location)

    @Delete
    suspend fun unCacheLocation(location: Location)

    @Query("SELECT * FROM Location where locationId=:locationId")
    suspend fun getCachedLocation(locationId: String): Location?

}