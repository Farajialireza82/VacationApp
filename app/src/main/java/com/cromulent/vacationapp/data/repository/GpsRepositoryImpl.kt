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
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.util.Constants
import com.cromulent.vacationapp.util.Constants.GPS_SETTINGS
import com.cromulent.vacationapp.util.Constants.USER_SETTINGS
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okio.IOException
import java.util.Locale

class GpsRepositoryImpl(
    val application: Application,
    val openWeatherMapApi: OpenWeatherMapApi
) : GpsRepository {

    private val _currentCoordinates =
        MutableStateFlow<CoordinatesData>(CoordinatesData(latitude = "", longitude = ""))
    override val currentCoordinates: StateFlow<CoordinatesData?> = _currentCoordinates.asStateFlow()

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = GPS_SETTINGS)

    private object PreferencesKeys {
        val CURRENT_COORDINATES = stringPreferencesKey(Constants.CURRENT_COORDINATES)
    }

    init {
        repositoryScope.launch {
            readCurrentCoordinates().collect { coordinates ->
                _currentCoordinates.value = coordinates
            }
        }
    }

    private var fusedLocationClient = LocationServices
        .getFusedLocationProviderClient(application)

    @SuppressLint("MissingPermission")
    override fun locateUser(onUserLocated: (CoordinatesData) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                repositoryScope.launch {

                    delay(1000)

                    searchForCoordinatesName(
                        location.latitude.toString(),
                        location.longitude.toString(),
                        limit = 1
                    ).collect {
                        val coordinates = it[0] ?: CoordinatesData(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                        saveCurrentCoordinates(coordinates)
                        onUserLocated(coordinates)
                    }
                }
            }
    }

    suspend fun searchForCoordinatesName(
        latitude: String,
        longitude: String,
        limit: Int = 1
    ): Flow<List<CoordinatesData?>> {

        val coordinatesData = try {
            openWeatherMapApi.searchForCoordinatesName(
                latitude = latitude,
                longitude = longitude,
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

    override suspend fun saveCurrentCoordinates(currentCoordinates: CoordinatesData) {

        _currentCoordinates.value = currentCoordinates

        val coordinatesJsonString = Gson().toJson(currentCoordinates, CoordinatesData::class.java)
        application.dataStore.edit { settings ->
            settings[PreferencesKeys.CURRENT_COORDINATES] = coordinatesJsonString
        }
    }

    override fun readCurrentCoordinates(): Flow<CoordinatesData> {
        val coordinates = application.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.CURRENT_COORDINATES] ?: ""
        }.map {
            try {

                Gson().fromJson<CoordinatesData>(
                    it,
                    CoordinatesData::class.java
                )
            } catch (_: Exception) {
                CoordinatesData("", "")
            }
        }
        return coordinates
    }
}