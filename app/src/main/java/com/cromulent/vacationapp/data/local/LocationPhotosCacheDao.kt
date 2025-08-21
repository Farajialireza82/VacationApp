package com.cromulent.vacationapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cromulent.vacationapp.data.model.LocationPhotoListEntity

@Dao
interface LocationPhotosCacheDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun cacheLocationPhotos(locationPhotos: LocationPhotoListEntity)

    @Query("SELECT * FROM LocationPhotoListEntity where locationId=:locationId")
    suspend fun getCachedLocationPhotos(locationId: String): LocationPhotoListEntity?

}