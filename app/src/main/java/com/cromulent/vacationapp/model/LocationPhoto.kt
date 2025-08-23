package com.cromulent.vacationapp.model

import com.google.gson.annotations.SerializedName

data class LocationPhoto(
    @SerializedName("id") val id: Long,
    @SerializedName("caption") val caption: String,
    @SerializedName("images") val images: Images
)

data class Images(
    @SerializedName("thumbnail") val thumbnail: ImageData?,
    @SerializedName("small") val small: ImageData?,
    @SerializedName("medium") val medium: ImageData?,
    @SerializedName("large") val large: ImageData?,
    @SerializedName("original") val original: ImageData?
)

data class ImageData(
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("url") val url: String
)