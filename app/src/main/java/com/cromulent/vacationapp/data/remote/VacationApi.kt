package com.cromulent.vacationapp.data.remote

import com.cromulent.vacationapp.data.remote.dto.Response
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VacationApi {

    @GET("location/nearby_search")
    suspend fun getNearbyLocations(
        @Query("key") key: String = API_KEY,
        @Query("latLong") latLong: String,
        @Query("category") category: String? = null
    ): Response<List<Location?>?>

    @GET("location/{location_id}/photos")
    suspend fun getLocationPhotos(
        @Path("location_id") locationId: String,
        @Query("key") key: String = API_KEY,
    ): Response<List<LocationPhoto>>
}