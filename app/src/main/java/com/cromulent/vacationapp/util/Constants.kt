package com.cromulent.vacationapp.util

import com.cromulent.vacationapp.BuildConfig
import com.cromulent.vacationapp.model.Category

object Constants {

    const val USER_SETTINGS = "userSettings"
    const val GPS_SETTINGS = "gpsSettings"
    const val APP_ENTRY = "appEntry"
    const val CURRENT_COORDINATES = "currentCoordinates"
    const val TRIP_ADVISOR_API_KEY = BuildConfig.TRIP_ADVISOR_API_KEY
    const val TRIP_ADVISOR_URL =  "https://api.content.tripadvisor.com/api/v1/"

    const val OPEN_WEATHER_MAP_URL = "https://api.openweathermap.org/geo/1.0/"
    const val OPEN_WEATHER_MAP_API_KEY = BuildConfig.OPEN_WEATHER_MAP_API_KEY


    const val LOCATION_DATABASE_NAME = "location-db"

    val CATEGORIES = listOf(
        Category(key = "hotels", title =  "Hotels"),
        Category(key = "attractions", title =  "Attractions"),
        Category(key = "restaurants", title =  "Restaurants"),
        Category(key = "geos", title =  "Nature"),
    )

    val SEARCH_CATEGORIES = listOf(Category("all", "All")) + CATEGORIES
}


