package com.cromulent.vacationapp.data.remote

import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.util.Constants.OPEN_WEATHER_MAP_API_KEY
import com.cromulent.vacationapp.util.Constants.TRIP_ADVISOR_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("direct")
    suspend fun searchForCoordinatesData(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("appId") appId: String = OPEN_WEATHER_MAP_API_KEY,
    ): List<CoordinatesData>


    @GET("reverse")
    suspend fun searchForCoordinatesName(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("limit") limit: Int = 1,
        @Query("appId") appId: String = OPEN_WEATHER_MAP_API_KEY,
    ): List<CoordinatesData>?
}