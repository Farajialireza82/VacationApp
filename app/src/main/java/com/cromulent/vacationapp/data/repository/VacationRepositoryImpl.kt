package com.cromulent.vacationapp.data.repository

import coil.network.HttpException
import com.cromulent.vacationapp.data.local.LocationCacheDao
import com.cromulent.vacationapp.data.local.LocationPhotosCacheDao
import com.cromulent.vacationapp.data.model.LocationPhotoListEntity
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class VacationRepositoryImpl(
    val vacationApi: VacationApi,
    val locationCacheDao: LocationCacheDao,
    val locationPhotoCacheDao: LocationPhotosCacheDao,
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

        val cachedLocation = getLocationFromCache(locationId)
        if (cachedLocation != null) {
            return flow {
                emit(cachedLocation)
            }
        }

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

        val locationPhotos = getLocationPhotos(locationId)


        response?.let {

            locationPhotos.collect {
                response.locationPhotos = it
            }

            cacheLocation(it)
        }


        return flow {
            emit(response)
        }
    }

    override suspend fun getLocationPhotos(locationId: String?): Flow<List<LocationPhoto>?> {
        if (locationId == null) return flow { }

        val cachedLocationPhotos = getLocationPhotosFromCache(locationId)
        if (cachedLocationPhotos != null) {
            return flow {
                emit(cachedLocationPhotos)
            }
        }

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

        cachedLocationPhotos?.let {

            cacheLocationPhotos(locationId, it)
        }

        return flow {
            locationPhotos.data?.let {
                emit(it)
            }
        }
    }

    suspend fun cacheLocation(location: Location) {
        locationCacheDao.cacheLocation(location)
    }

    suspend fun getLocationFromCache(locationId: String): Location? {
        return locationCacheDao.getCachedLocation(locationId)
    }

    suspend fun cacheLocationPhotos(locationId: String, location: List<LocationPhoto>) {
        locationPhotoCacheDao.cacheLocationPhotos(
            LocationPhotoListEntity(
                locationId,
                location
            )
        )
    }

    suspend fun getLocationPhotosFromCache(locationId: String): List<LocationPhoto>? {
        return locationPhotoCacheDao.getCachedLocationPhotos(locationId)?.locationPhotos
    }
}