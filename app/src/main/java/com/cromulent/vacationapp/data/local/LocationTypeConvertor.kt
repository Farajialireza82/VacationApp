package com.cromulent.vacationapp.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.cromulent.vacationapp.data.model.LocationPhotoListEntity
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class LocationTypeConvertor {

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return if (value == null) {
            null
        } else {
            val listType = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun fromLocationPhotoList(value: List<LocationPhoto>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun toLocationPhotoList(value: String?): List<LocationPhoto>? {
        return if (value == null) {
            null
        } else {
            val listType = object : TypeToken<List<LocationPhoto>>() {}.type
            Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun fromLocationPhotoListEntity(value: LocationPhotoListEntity?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun toLocationPhotoListEntity(value: String?): LocationPhotoListEntity? {
        return if (value == null) {
            null
        } else {
            val listType = object : TypeToken<List<LocationPhotoListEntity>>() {}.type
            Gson().fromJson(value, listType)
        }
    }

}