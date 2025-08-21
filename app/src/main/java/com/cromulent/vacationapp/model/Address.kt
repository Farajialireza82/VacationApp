package com.cromulent.vacationapp.model

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("street1")
    val street1: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("postalcode")
    val postalcode: String?,
    @SerializedName("address_string")
    val addressString: String?
){

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