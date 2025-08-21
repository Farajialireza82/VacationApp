package com.cromulent.vacationapp.data.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Geocoder
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import coil.network.HttpException
import com.cromulent.vacationapp.data.remote.OpenWeatherMapApi
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.repository.OpenWeatherMapRepository
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.util.Constants
import com.cromulent.vacationapp.util.Constants.GPS_SETTINGS
import com.cromulent.vacationapp.util.Constants.USER_SETTINGS
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okio.IOException
import java.util.Locale

class OpenWeatherMapRepositoryImpl(
    val openWeatherMapApi: OpenWeatherMapApi
) : OpenWeatherMapRepository {

    override suspend fun searchForCoordinatesData(
        query: String,
        limit: Int
    ): Flow<List<CoordinatesData?>> {

        val coordinatesData = try {
            openWeatherMapApi.searchForCoordinatesData(
                query = query,
                limit = limit
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return flow { emit(listOf<CoordinatesData>()) }

        } catch (e: HttpException) {
            e.printStackTrace()
            return flow { listOf<CoordinatesData>() }

        } catch (e: Exception) {
            e.printStackTrace()
            return flow { listOf<CoordinatesData>() }

        }
        return flow {
            emit(coordinatesData ?: listOf())
        }

    }

}