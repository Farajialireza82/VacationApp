package com.cromulent.vacationapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoordinatesData(
    @SerializedName("lat") val latitude: String,
    @SerializedName("lng") val longitude: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("country") val country: String? = null,
) : Parcelable {
    fun getCoordinatesString(): String = "$latitude,$longitude"
}
