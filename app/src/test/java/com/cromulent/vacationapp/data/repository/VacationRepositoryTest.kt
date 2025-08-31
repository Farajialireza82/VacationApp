package com.cromulent.vacationapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cromulent.vacationapp.data.local.LocationCacheDao
import com.cromulent.vacationapp.data.local.LocationPhotosCacheDao
import com.cromulent.vacationapp.data.model.LocationPhotoListEntity
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.data.remote.dto.Response
import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.ImageData
import com.cromulent.vacationapp.model.Images
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VacationRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: VacationRepositoryImpl
    private lateinit var vacationApi: VacationApi
    private lateinit var locationCacheDao: LocationCacheDao
    private lateinit var locationPhotosCacheDao: LocationPhotosCacheDao

    @Before
    fun setup() {
        vacationApi = mockk()
        locationCacheDao = mockk()
        locationPhotosCacheDao = mockk()

        repository = VacationRepositoryImpl(
            vacationApi = vacationApi,
            locationCacheDao = locationCacheDao,
            locationPhotoCacheDao = locationPhotosCacheDao
        )

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun getNearbyLocationsShouldReturnSuccessWhenApiCallSucceeds() = runTest {

        val mockLocations = createMockLocations()

        coEvery {
            vacationApi.getNearbyLocations(any(), any(), any())
        } returns Response(mockLocations)

        val result = repository.getNearbyLocations("37.7749,-122.4194", "restaurant").first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(mockLocations)

        coVerify {
            repository.getNearbyLocations("37.7749,-122.4194", "restaurant")
        }

    }

    @Test
    fun getNearbyLocationShouldReturnErrorWhenExceptionOccurs() = runTest {
        val errorMessage = "Something went wrong"

        coEvery {
            vacationApi.getNearbyLocations(any(),any(), any())
        } throws RuntimeException(errorMessage)

        val result = repository.getNearbyLocations("37.7749,-122.4194", "restaurant").first()

        // Assert
        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).contains("Something went wrong")

    }

    @Test
    fun getLocationDetailsShouldReturnCachedLocationWhenForceRefreshIsFalse() = runTest {

        val locationId = "123456"
        val cachedLocation = createMockLocation(locationId)

        coEvery {
            locationCacheDao.getCachedLocation(locationId)
        } returns cachedLocation

        val result = repository.getLocationDetails(locationId, false).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data).isEqualTo(cachedLocation)

        coVerify(exactly = 0) { vacationApi.getLocationDetails(locationId) }

    }

    @Test
    fun getLocationDetailsShouldFetchFromApiWhenNotCached() = runTest {

        val locationId = "123456"
        val mockLocation = createMockLocation(locationId)
        val mockPhotos = createMockLocationPhotos()

        coEvery { locationCacheDao.getCachedLocation(locationId) } returns null
        coEvery { vacationApi.getLocationDetails(locationId, key = any()) } returns mockLocation
        coEvery { locationPhotosCacheDao.getCachedLocationPhotos(locationId) } returns LocationPhotoListEntity(
            locationId,
            mockPhotos
        )
        coEvery { locationCacheDao.cacheLocation(any()) } just Runs

        val result = repository.getLocationDetails(locationId, forceRefresh = false).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data?.locationId).isEqualTo(locationId)

        coVerify { vacationApi.getLocationDetails(locationId) }
        coVerify { locationCacheDao.cacheLocation(any()) }


    }

    @Test
    fun getLocationDetailsShouldForceRefreshWhenForceRefreshIsTrue() = runTest {

        val locationId = "123456"
        val cachedLocation = createMockLocation(locationId)
        val apiLocation = createMockLocation(locationId, "Updated Name")
        val mockPhotos = createMockLocationPhotos()

        coEvery { locationCacheDao.getCachedLocation(locationId) } returns cachedLocation
        coEvery { vacationApi.getLocationDetails(locationId, key = any()) } returns apiLocation
        coEvery { locationPhotosCacheDao.getCachedLocationPhotos(locationId) } returns LocationPhotoListEntity(
            locationId,
            mockPhotos
        )
        coEvery { locationCacheDao.cacheLocation(any()) } just Runs

        val result = repository.getLocationDetails(locationId, forceRefresh = true).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data?.name).isEqualTo("Updated Name")

        coVerify { vacationApi.getLocationDetails(locationId) }
        coVerify { locationCacheDao.cacheLocation(any()) }

    }

    @Test
    fun searchLocationShouldReturnSuccessWithData() = runTest {

        val query = "testQuery"
        val category = "testCategory"


        val mockLocations = createMockLocations()
        val mockResponse = Response(mockLocations)

        coEvery { vacationApi.searchLocation(category = category, query = query, key = any()) } returns mockResponse

        val result = repository.searchLocation(category = category, query = query).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data).isEqualTo(mockLocations)

        coVerify { vacationApi.searchLocation(category = category, query =  query, key = any()) }


    }

    @Test
    fun searchLocationShouldPassNullWhenCategoryIsAll() = runTest {

        val query = "q"
        val category = "all"
        val mockLocations = createMockLocations()

        coEvery {
            vacationApi.searchLocation(category = null, query = query, key = any())
        } returns Response(mockLocations)

        val result = repository.searchLocation(category = category, query = query).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(mockLocations)

        coVerify { vacationApi.searchLocation(query = query, category = null, key = any()) }

    }

    @Test
    fun getLocationPhotosShouldReturnCachedPhotosWhenAvailable() = runTest {

        val locationId = "123456"
        val cachedPhotos = createMockLocationPhotos()

        coEvery { locationPhotosCacheDao.getCachedLocationPhotos(locationId) } returns LocationPhotoListEntity(
            locationId,
            cachedPhotos
        )

        val result = repository.getLocationPhotos(locationId).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(cachedPhotos)

        coVerify(exactly = 0) { vacationApi.getLocationDetails(any(), any()) }

    }

    @Test
    fun getLocationPhotosShouldFetchFromApiWhenNotCached() = runTest {

        val locationId = "123456"
        val cachedPhotos = createMockLocationPhotos()
        val mockedResponse = Response(cachedPhotos)

        coEvery { locationPhotosCacheDao.getCachedLocationPhotos(locationId) } returns null
        coEvery { vacationApi.getLocationPhotos(locationId, key = any()) } returns mockedResponse
        coEvery { locationPhotosCacheDao.cacheLocationPhotos(any()) } just Runs

        val result = repository.getLocationPhotos(locationId).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat((result as Resource.Success).data).isEqualTo(cachedPhotos)

        coVerify { vacationApi.getLocationPhotos(locationId) }
        coVerify { locationPhotosCacheDao.cacheLocationPhotos(any()) }
    }

    @Test
    fun getLocationPhotosShouldHandleApiError() = runTest {

        val locationId = "123456"
        val errorMessage = "Something went wrong"

        coEvery { vacationApi.getLocationPhotos(locationId = locationId, key = any()) } throws RuntimeException(errorMessage)
        coEvery { locationPhotosCacheDao.getCachedLocationPhotos(locationId = locationId) } returns null

        val result = repository.getLocationPhotos(locationId).first()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat((result as Resource.Error).message).isEqualTo(errorMessage)


    }

    @Test
    fun cacheLocationShouldCallDAOCacheFunction() = runTest {

        val locationId = "1234"
        val mockLocation = createMockLocation(locationId)

        coEvery { locationCacheDao.cacheLocation(mockLocation) } just Runs

        repository.cacheLocation(mockLocation)

        coVerify { locationCacheDao.cacheLocation(any()) }

    }

    @Test
    fun getLocationFromCacheShouldCallDAOGetFunction() = runTest{

        val locationId = "1234"
        val mockLocation = createMockLocation(locationId)

        coEvery { locationCacheDao.getCachedLocation(locationId) } returns mockLocation

        val result = repository.getLocationDetails(locationId).first()

        assertThat(result.data).isEqualTo(mockLocation)
        coVerify { locationCacheDao.getCachedLocation(locationId) }

    }


    // Helper functions
    private fun createMockLocations(): List<Location> {
        return listOf(
            createMockLocation("1"),
            createMockLocation("2")
        )
    }

    private fun createMockLocation(id: String, name: String = "Test Location $id"): Location {
        return Location(
            locationId = id,
            name = name,
            distance = "1.2",
            description = "Test location $id",
            reviewCount = "100",
            rating = "4.5",
            latitude = "37.7749",
            longitude = "-122.4194",
            seeAllPhotosLink = "https://example.com/photos$id",
            priceLevel = "$$$",
            locationPhotos = null,
            amenities = listOf("WiFi", "Parking"),
            addressObject = Address(
                street1 = "123 Test St",
                city = "San Francisco",
                state = "CA",
                country = "US",
                postalcode = "94105",
                addressString = "123 Test St, San Francisco, CA 94105"
            )
        )
    }

    private fun createMockLocationPhotos(): List<LocationPhoto> {
        return listOf(
            LocationPhoto(
                id = 1L,
                caption = "Beautiful view",
                images = Images(
                    thumbnail = ImageData(150, 150, "https://example.com/thumb.jpg"),
                    small = ImageData(300, 200, "https://example.com/small.jpg"),
                    medium = ImageData(600, 400, "https://example.com/medium.jpg"),
                    large = ImageData(1200, 800, "https://example.com/large.jpg"),
                    original = ImageData(2400, 1600, "https://example.com/original.jpg")
                )
            ),
            LocationPhoto(
                id = 2L,
                caption = "Another view",
                images = Images(
                    thumbnail = ImageData(150, 150, "https://example.com/thumb2.jpg"),
                    small = ImageData(300, 200, "https://example.com/small2.jpg"),
                    medium = ImageData(600, 400, "https://example.com/medium2.jpg"),
                    large = ImageData(1200, 800, "https://example.com/large2.jpg"),
                    original = ImageData(2400, 1600, "https://example.com/original2.jpg")
                )
            )
        )
    }
}