package com.cromulent.vacationapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoordinatesData(
    @SerializedName("lat") val latitude: String,
    @SerializedName("lon") val longitude: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("country") val country: String? = null,
) : Parcelable {
    fun getCoordinatesString(): String = "$latitude,$longitude"

    fun getTitle(): String {
        return when {
            // All three available: name, state, country
            !name.isNullOrBlank() && !state.isNullOrBlank() && !country.isNullOrBlank() -> {
                "$name, $state, $country"
            }
            // Name and country available, state is null/blank
            !name.isNullOrBlank() && !country.isNullOrBlank() && state.isNullOrBlank() -> {
                "$name, $country"
            }
            // Name and state available, country is null/blank
            !name.isNullOrBlank() && !state.isNullOrBlank() && country.isNullOrBlank() -> {
                "$name, $state"
            }
            // Only name available
            !name.isNullOrBlank() && state.isNullOrBlank() && country.isNullOrBlank() -> {
                name
            }
            // No name but have state and country
            name.isNullOrBlank() && !state.isNullOrBlank() && !country.isNullOrBlank() -> {
                "$state, $country"
            }
            // Only state available
            name.isNullOrBlank() && !state.isNullOrBlank() && country.isNullOrBlank() -> {
                state
            }
            // Only country available
            name.isNullOrBlank() && state.isNullOrBlank() && !country.isNullOrBlank() -> {
                country
            }
            // All are null/blank - fallback to coordinates (first 6 chars each)
            else -> {
                val latShort = latitude.take(6)
                val lngShort = longitude.take(6)
                "$latShort, $lngShort"
            }
        }
    }

    fun getAddressTitle(): String {
        return when {
            // Both state and country available
            !state.isNullOrBlank() && !country.isNullOrBlank() -> {
                "$state, $country"
            }
            // Only state available
            !state.isNullOrBlank() && country.isNullOrBlank() -> {
                state
            }
            // Only country available
            state.isNullOrBlank() && !country.isNullOrBlank() -> {
                country
            }
            else  -> "NULL"
        }
    }
}
