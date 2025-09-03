package com.cromulent.vacationapp.presentation.searchScreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.repository.OpenWeatherMapRepository
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.model.ImageData
import com.cromulent.vacationapp.model.Images
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.presentation.gpsScreen.GpsViewmodel
import com.cromulent.vacationapp.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewmodelTest {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewmodel: SearchViewmodel
    private lateinit var vacationRepository: VacationRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        vacationRepository = mockk()

        viewmodel = SearchViewmodel(vacationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun initialStateIsCorrect() {

        val state = viewmodel.state.value

        assertThat(state.data).isNull()
        assertThat(state.error).isNull()
        assertThat(state.isLoading).isFalse()

    }

    @Test
    fun searchFunctionCallsRepositoriesSearchFunctionCorrectly() = runTest{

        val category = "category"
        val query = "query"

        coEvery {
            vacationRepository.searchLocation(any(), any())
        } returns MutableStateFlow(Resource.Success(createMockLocations()))

        viewmodel.search(category, query)

        advanceUntilIdle()

        coVerify (exactly = 1) {
            vacationRepository.searchLocation(category, query)
        }

    }

    @Test
    fun searchFunctionUpdatesStateWithDataCorrectly() = runTest {

        val data = createMockLocations()

        coEvery {
            vacationRepository.searchLocation(any(), any())
        } returns MutableStateFlow(Resource.Success(data))

        viewmodel.search("category", "query")

        advanceUntilIdle()

        val state = viewmodel.state.value

        assertThat(state.data).isEqualTo(data)
        assertThat(state.error).isNull()
        assertThat(state.isLoading).isFalse()

    }

    @Test
    fun searchFunctionHandlesErrorsCorrectly() = runTest {

        val errorMessage = "something went wrong"

        coEvery {
            vacationRepository.searchLocation(any(), any())
        } returns MutableStateFlow(Resource.Error(errorMessage))

        viewmodel.search("category", "query")

        advanceUntilIdle()

        val state = viewmodel.state.value

        assertThat(state.data).isNull()
        assertThat(state.error).isEqualTo(errorMessage)
        assertThat(state.isLoading).isFalse()

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