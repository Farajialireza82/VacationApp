package com.cromulent.vacationapp.data.repository

import coil.network.HttpException
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class VacationRepositoryImpl(
    val vacationApi: VacationApi
) : VacationRepository {

    override suspend fun getNearbyLocations(
        latLng: String,
        category: String?
    ): Flow<List<Location?>> {

        val locations = try {
            vacationApi.getNearbyLocations(
                latLong = latLng,
                category = category
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return flow { emit(listOf<Location>()) }

        } catch (e: HttpException) {
            e.printStackTrace()
            return flow { listOf<Location>() }

        } catch (e: Exception) {
            e.printStackTrace()
            return flow { listOf<Location>() }

        }
        return flow {
            locations.data?.let {
                emit(it)
            }
        }
    }

    override suspend fun getLocationDetails(locationId: String): Flow<Location?> {
        val response = try {
            vacationApi.getLocationDetails(locationId = locationId)
        } catch (e: IOException) {
            e.printStackTrace()
            return flow { emit(null) }

        } catch (e: HttpException) {
            e.printStackTrace()
            return flow { emit(null) }

        } catch (e: Exception) {
            e.printStackTrace()
            return flow { emit(null) }

        }
        return flow {
            emit(response)
        }
    }

    override suspend fun getLocationPhotos(locationId: String?): Flow<List<LocationPhoto>?> {
        if (locationId == null) return flow { }
        val locationPhotos = try {
            vacationApi.getLocationPhotos(locationId = locationId)
        } catch (e: IOException) {
            e.printStackTrace()
            return flow { emit(null) }

        } catch (e: HttpException) {
            e.printStackTrace()
            return flow { emit(null) }

        } catch (e: Exception) {
            e.printStackTrace()
            return flow { emit(null) }

        }
        return flow {
            locationPhotos.data?.let {
                emit(it)
            }
        }
    }
}