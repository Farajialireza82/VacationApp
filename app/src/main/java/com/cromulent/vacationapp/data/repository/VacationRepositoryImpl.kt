package com.cromulent.vacationapp.data.repository

import coil.network.HttpException
import com.cromulent.vacationapp.data.local.LocationCacheDao
import com.cromulent.vacationapp.data.local.LocationPhotosCacheDao
import com.cromulent.vacationapp.data.model.LocationPhotoListEntity
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.data.remote.dto.Response
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.util.Resource
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
    ): Flow<Resource<List<Location?>>> {

        val locations = try {
            vacationApi.getNearbyLocations(
                latLong = latLng,
                category = category
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
        return flow {
            locations.data?.let {
                emit(
                    Resource.Success(data = it)
                )
            }
        }
    }

    override suspend fun getLocationDetails(
        locationId: String,
        forceRefresh: Boolean
    ): Flow<Resource<Location?>> {

        if(forceRefresh.not()) {
            val cachedLocation = getLocationFromCache(locationId)
            if (cachedLocation != null) {
                return flow {
                    emit(
                        Resource<Location?>.Success(cachedLocation)
                    )
                }
            }
        }

        val response = try {
            vacationApi.getLocationDetails(locationId = locationId)
        } catch (e: IOException) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        }

        val locationPhotos = getLocationPhotos(locationId)


        response?.let {

            locationPhotos.collect {
                when (it) {
                    is Resource.Error<*> -> {

                    }

                    is Resource.Success<*> -> {
                        response.locationPhotos = it.data
                    }
                }
            }

            cacheLocation(it)
        }


        return flow {
            emit(
                Resource.Success(response)
            )
        }
    }

    override suspend fun searchLocation(
        category: String,
        query: String
    ): Flow<Resource<List<Location>>> {
        val locations = try {
            vacationApi.searchLocation(
                query = query,
                category = if(category == "all" ) null else category
            )
        } catch (e: IOException) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
        return flow {
            locations.data?.let {
                emit(
                    Resource.Success(data = it)
                )
            }
        }
    }

    override suspend fun getLocationPhotos(locationId: String?): Flow<Resource<List<LocationPhoto>?>> {
        if (locationId == null) return flow { }

        val cachedLocationPhotos = getLocationPhotosFromCache(locationId)
        if (cachedLocationPhotos != null) {
            return flow {
                emit(
                    Resource.Success(cachedLocationPhotos)
                )
            }
        }

        val locationPhotos = try {
            vacationApi.getLocationPhotos(locationId = locationId)
        } catch (e: IOException) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        } catch (e: HttpException) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return flow {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }

        }

        if(locationPhotos.data.isNullOrEmpty().not()){

            cacheLocationPhotos(locationId, locationPhotos.data)

        }

        return flow {
            locationPhotos.data?.let {
                emit(Resource.Success(it))
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