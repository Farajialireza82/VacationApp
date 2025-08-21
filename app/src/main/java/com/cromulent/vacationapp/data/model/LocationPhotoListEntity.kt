package com.cromulent.vacationapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cromulent.vacationapp.model.LocationPhoto

@Entity
data class LocationPhotoListEntity(
    @PrimaryKey val locationId: String,
    val locationPhotos: List<LocationPhoto>
)
