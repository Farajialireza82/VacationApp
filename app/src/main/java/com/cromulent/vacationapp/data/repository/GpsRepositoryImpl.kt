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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale

class GpsRepositoryImpl(
    val application: Application
) : GpsRepository {

    private val _currentCoordinates =
        MutableStateFlow<CoordinatesData>(CoordinatesData(latitude = "", longitude = ""))
    override val currentCoordinates: StateFlow<CoordinatesData> = _currentCoordinates.asStateFlow()

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
    override fun locateUser(onUserLocated: () -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                val coordinates = CoordinatesData(
                    latitude = location.latitude.toString(),
                    longitude = location.longitude.toString()
                )
                val locationString =
                    "${location.latitude.toString()},${location.longitude.toString()}"
                _currentCoordinates.value = coordinates


                val geoCoder = Geocoder(application, Locale.getDefault())
                val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName = addresses?.get(0)?.getAddressLine(0)
                val countryName = addresses?.get(0)?.getAddressLine(1)


                repositoryScope.launch {
                    saveCurrentCoordinates(coordinates)
                }

                onUserLocated()
            }
    }

    override suspend fun saveCurrentCoordinates(currentCoordinates: CoordinatesData) {

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
            }catch (e: Exception){
                CoordinatesData("", "")
            }
        }
        return coordinates
    }
}