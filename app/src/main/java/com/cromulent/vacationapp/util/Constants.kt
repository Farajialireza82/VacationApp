package com.cromulent.vacationapp.util

import com.cromulent.vacationapp.BuildConfig

object Constants {

    const val USER_SETTINGS = "userSettings"
    const val APP_ENTRY = "appEntry"
    const val API_KEY = BuildConfig.API_KEY

    const val BASE_URL =  "https://api.content.tripadvisor.com/api/v1/"

    val CATEGORIES = listOf(
        Category(key = "hotels", title =  "Hotels"),
        Category(key = "attractions", title =  "Attractions"),
        Category(key = "restaurants", title =  "Restaurants"),
        Category(key = "geos", title =  "Nature"),
    )
}

data class Category(val key: String, val title: String)
