package com.cromulent.vacationapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Location(

    @PrimaryKey
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("distance")
    val distance: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("num_reviews")
    val reviewCount: String? = null,
    @SerializedName("rating")
    val rating: String? = null,
    @SerializedName("latitude")
    val latitude: String? = null,
    @SerializedName("longitude")
    val longitude: String? = null,
    @SerializedName("amenities")
    val amenities: List<String>? = null,
    @SerializedName("see_all_photos")
    val seeAllPhotosLink: String? = null,
    @SerializedName("web_url")
    val webUrl: String? = null,
    @SerializedName("price_level")
    val priceLevel: String? = null,
    var locationPhotos: List<LocationPhoto>? = null
)