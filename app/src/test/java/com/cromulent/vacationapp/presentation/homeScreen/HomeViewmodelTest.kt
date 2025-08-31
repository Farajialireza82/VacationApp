package com.cromulent.vacationapp.presentation.homeScreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.model.ImageData
import com.cromulent.vacationapp.model.Images
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.util.Resource
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewmodelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewmodel: HomeViewmodel
    private lateinit var vacationRepository: VacationRepository
    private lateinit var gpsRepository: GpsRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        vacationRepository = mockk()
        gpsRepository = mockk()

        every { gpsRepository.currentCoordinates }
            .returns(
                MutableStateFlow(
                    CoordinatesData(
                        latitude = "37.7749",
                        longitude = "-122.4194",
                        name = "San Francisco"
                    )
                )
            )

        viewmodel = HomeViewmodel(vacationRepository, gpsRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun initialStateShouldBeCorrect() {
        val initialState = viewmodel.state.value

        assertThat(initialState.popularLocations).isEmpty()
        assertThat(initialState.recommendedLocations).isEmpty()
        assertThat(initialState.error).isNull()
        assertThat(initialState.isLoading).isFalse()


    }

    @Test
    fun getNearbyLocationsShouldUpdateLoadingStateCorrectly() = runTest {

        val mockLocations = createMockLocations()

        coEvery {
            vacationRepository.getNearbyLocations(any(), any())
        } returns flowOf(Resource.Success(mockLocations))

        coEvery {
            vacationRepository.getLocationPhotos(any())
        } returns flowOf(Resource.Success(emptyList()))

        viewmodel.getNearbyLocations("restaurant")

        assertThat(viewmodel.state.value.isLoading).isTrue()

        advanceUntilIdle()


        assertThat(viewmodel.state.value.isLoading).isFalse()
        assertThat(viewmodel.state.value.recommendedLocations).isNotEmpty()
        assertThat(viewmodel.state.value.popularLocations).isNotEmpty()
        assertThat(viewmodel.state.value.error).isNull()

    }

    @Test
    fun getNearbyLocationsShouldHandleErrorsCorrectly() = runTest {

        val errorMessage = "Something went wrong"

        coEvery {
            vacationRepository.getNearbyLocations(any(), any())
        } returns flowOf(Resource.Error(errorMessage))

        viewmodel.getNearbyLocations("restaurant")

        advanceUntilIdle()

        assertThat(viewmodel.state.value.isLoading).isFalse()
        assertThat(viewmodel.state.value.error).isEqualTo(errorMessage)
        assertThat(viewmodel.state.value.popularLocations).isEmpty()
        assertThat(viewmodel.state.value.recommendedLocations).isEmpty()

    }

    @Test
    fun getNearbyLocationsShouldUseCacheWhenAvailable() = runTest {

        val mockLocations = createMockLocations()
        coEvery {
            vacationRepository.getNearbyLocations(any(), any())
        } returns flowOf(Resource.Success(mockLocations))

        coEvery {
            vacationRepository.getLocationPhotos(any())
        } returns flowOf(Resource.Success(emptyList()))

        viewmodel.getNearbyLocations("restaurant")
        advanceUntilIdle()

        coVerify(exactly = 1) {
            vacationRepository.getNearbyLocations(any(), "restaurant")
        }

        viewmodel.getNearbyLocations("restaurant")
        advanceUntilIdle()

        coVerify(
            exactly = 1
        ) { vacationRepository.getNearbyLocations(any(), "restaurant") }

        assertThat(viewmodel.state.value.popularLocations).isEqualTo(mockLocations)

    }

    @Test
    fun getNearbyLocationsShouldClearCacheWhenClearCacheIsTrue() = runTest {

        val mockLocations = createMockLocations()
        coEvery {
            vacationRepository.getNearbyLocations(any(), any())
        } returns flowOf(Resource.Success(mockLocations))

        coEvery {
            vacationRepository.getLocationPhotos(any())
        } returns flowOf(Resource.Success(emptyList()))


        viewmodel.getNearbyLocations("restaurant")
        advanceUntilIdle()


        viewmodel.getNearbyLocations("restaurant", clearCache = true)
        advanceUntilIdle()

        coVerify(
            exactly = 2
        ) { vacationRepository.getNearbyLocations(any(), "restaurant") }

    }

    @Test
    fun getNearbyLocationsShouldFetchLocationPhotos() = runTest {

        val mockLocations = createMockLocations()
        val mockPhotos = createMockLocationPhotos()

        coEvery {
            vacationRepository.getNearbyLocations(any(), any())
        } returns flowOf(Resource.Success(mockLocations))

        coEvery {
            vacationRepository.getLocationPhotos(any())
        } returns flowOf(Resource.Success(mockPhotos))

        viewmodel.getNearbyLocations("restaurant")
        advanceUntilIdle()

        coVerify {
            vacationRepository.getLocationPhotos(mockLocations[0].locationId)
            vacationRepository.getLocationPhotos(mockLocations[1].locationId)
        }
    }

    @Test
    fun getNearbyLocationsShouldHandlePhotoFetchErrorCorrectly() = runTest {

        val mockLocations = createMockLocations()
        val photoErrorMessage = "Error Occurred"

        coEvery {
            vacationRepository.getNearbyLocations(any(), any())
        } returns flowOf(Resource.Success(mockLocations))

        coEvery {
            vacationRepository.getLocationPhotos(any())
        } returns flowOf(Resource.Error(photoErrorMessage))

        viewmodel.getNearbyLocations("restaurant")
        advanceUntilIdle()

        assertThat(viewmodel.state.value.error).isEqualTo(photoErrorMessage)
        assertThat(viewmodel.state.value.popularLocations).isNotEmpty()
        assertThat(viewmodel.state.value.recommendedLocations).isNotEmpty()

    }

    @Test
    fun currentCoordinatesShouldReturnGpsRepositoryCurrentCoordinates() = runTest {

        val testCoordinates = CoordinatesData(
            latitude = "12.3456",
            longitude = "54.1234",
            name = "Sample Location"
        )

        every {
            gpsRepository.currentCoordinates
        } returns MutableStateFlow(testCoordinates)

        viewmodel = HomeViewmodel(vacationRepository, gpsRepository)

        assertThat(viewmodel.currentCoordinates.value).isEqualTo(testCoordinates)

    }

    @Test fun differentCategoriesShouldBeCachedSeparately() = runTest {

        val restaurantLocations = createMockLocations("restaurant")
        val hotelLocations = createMockLocations("hotel")

        coEvery {
            vacationRepository.getNearbyLocations(any(), "restaurant")
        } returns flowOf(Resource.Success(restaurantLocations))

        coEvery {
            vacationRepository.getNearbyLocations(any(), "hotel")
        } returns flowOf(Resource.Success(hotelLocations))

        coEvery {
            vacationRepository.getLocationPhotos(any())
        } returns flowOf(Resource.Success(emptyList()))

        viewmodel.getNearbyLocations("restaurant")
        advanceUntilIdle()

        viewmodel.getNearbyLocations("hotel")
        advanceUntilIdle()

        viewmodel.getNearbyLocations("restaurant")
        advanceUntilIdle()

        coVerify(exactly = 1) { vacationRepository.getNearbyLocations(any(), "restaurant") }
        coVerify(exactly = 1) { vacationRepository.getNearbyLocations(any(), "hotel") }
        assertThat(viewmodel.state.value.popularLocations).isEqualTo(restaurantLocations)

    }


    private fun createMockLocations(prefix: String = "location"): List<Location> {
        return listOf(
            Location(
                locationId = "${prefix}_1",
                name = "$prefix Location 1",
                distance = "1.2",
                description = "Test $prefix location 1",
                reviewCount = "100",
                rating = "4.5",
                latitude = "37.7749",
                longitude = "-122.4194",
                seeAllPhotosLink = "https://example.com/photos1",
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
            ),
            Location(
                locationId = "${prefix}_2",
                name = "$prefix Location 2",
                distance = "2.1",
                description = "Test $prefix location 2",
                reviewCount = "200",
                rating = "4.0",
                latitude = "37.7849",
                longitude = "-122.4094",
                seeAllPhotosLink = "https://example.com/photos2",
                priceLevel = "$$",
                locationPhotos = null,
                amenities = listOf("Pool", "Gym"),
                addressObject = Address(
                    street1 = "456 Test Ave",
                    city = "San Francisco",
                    state = "CA",
                    country = "US",
                    postalcode = "94106",
                    addressString = "456 Test Ave, San Francisco, CA 94106"
                )
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
            )
        )
    }

}