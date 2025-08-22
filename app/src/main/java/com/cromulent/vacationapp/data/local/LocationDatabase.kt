package com.cromulent.vacationapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.cromulent.vacationapp.data.model.LocationPhotoListEntity
import com.cromulent.vacationapp.model.Location

@Database(entities = [Location::class, LocationPhotoListEntity::class], version = 4)
@TypeConverters(LocationTypeConvertor::class)
abstract class LocationDatabase: RoomDatabase() {

    abstract val locationCacheDao: LocationCacheDao
    abstract val locationPhotosCacheDao: LocationPhotosCacheDao
}