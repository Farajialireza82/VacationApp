package com.cromulent.vacationapp.presentation.gpsScreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.repository.OpenWeatherMapRepository
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
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
class GpsViewmodelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewmodel: GpsViewmodel
    private lateinit var gpsRepository: GpsRepository
    private lateinit var openWeatherMapRepository: OpenWeatherMapRepository

    private val testDispatcher = StandardTestDispatcher()

    private val sampleCoordinates = CoordinatesData(
        latitude = "37.7749",
        longitude = "-122.4194",
        name = "San Francisco"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        gpsRepository = mockk()
        openWeatherMapRepository = mockk()

        every { gpsRepository.currentCoordinates } returns MutableStateFlow(null)

        viewmodel = GpsViewmodel(
            gpsRepository = gpsRepository,
            openWeatherMapRepository = openWeatherMapRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun initialStateIsCorrect() {
        val state = viewmodel.state.value

        assertThat(state.isLocating).isFalse()
        assertThat(state.isCoordinateSelected).isFalse()
        assertThat(state.isSearching).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.searchResults).isEmpty()
    }

    @Test
    fun getCurrentLocationSetsLoadingState() = runTest {
        val callbackSlot = slot<(CoordinatesData) -> Unit>()
        coEvery { gpsRepository.locateUser(capture(callbackSlot)) } just Runs

        viewmodel.getCurrentLocation()

        assertThat(viewmodel.state.value.isLocating).isTrue()
        coVerify(exactly = 1) { gpsRepository.locateUser(any()) }
    }

    @Test
    fun getCurrentLocationSuccessUpdatesStateAndSavesCoordinates() = runTest {
        val callbackSlot = slot<(CoordinatesData) -> Unit>()
        coEvery { gpsRepository.locateUser(capture(callbackSlot)) } answers {
            callbackSlot.captured.invoke(sampleCoordinates)
        }
        coEvery { gpsRepository.saveCurrentCoordinates(any()) } just Runs

        viewmodel.getCurrentLocation()
        advanceUntilIdle()

        val state = viewmodel.state.value
        assertThat(state.isLocating).isFalse()
        assertThat(state.isCoordinateSelected).isTrue()

        coVerify(exactly = 1) { gpsRepository.locateUser(any()) }
        coVerify(exactly = 1) { gpsRepository.saveCurrentCoordinates(sampleCoordinates) }
    }

    @Test
    fun getCurrentLocationWithoutCallbackDoesNotUpdateCoordinateSelectedState() = runTest {
        coEvery { gpsRepository.locateUser(any()) } just Runs

        viewmodel.getCurrentLocation()
        advanceUntilIdle()

        val state = viewmodel.state.value
        assertThat(state.isLocating).isTrue()
        assertThat(state.isCoordinateSelected).isFalse()

        coVerify(exactly = 1) { gpsRepository.locateUser(any()) }
        coVerify(exactly = 0) { gpsRepository.saveCurrentCoordinates(any()) }
    }

    @Test
    fun setCurrentCoordinatesSavesDataAndUpdatesState() = runTest {
        coEvery { gpsRepository.saveCurrentCoordinates(any()) } just Runs

        viewmodel.setCurrentCoordinates(sampleCoordinates)
        advanceUntilIdle()

        assertThat(viewmodel.state.value.isCoordinateSelected).isTrue()
        coVerify(exactly = 1) { gpsRepository.saveCurrentCoordinates(sampleCoordinates) }
    }

    @Test
    fun searchSetsSearchingState() = runTest {
        coEvery { openWeatherMapRepository.searchForCoordinatesData(any()) } returns flowOf()

        viewmodel.search("test query")

        assertThat(viewmodel.state.value.isSearching).isTrue()

        advanceUntilIdle()

        coVerify(exactly = 1) { openWeatherMapRepository.searchForCoordinatesData("test query") }
    }

    @Test
    fun searchSuccessWithResultsUpdatesState() = runTest {
        val searchResults = listOf(
            sampleCoordinates,
            CoordinatesData("40.7128", "-74.0060", "New York")
        )

        coEvery {
            openWeatherMapRepository.searchForCoordinatesData(any())
        } returns flowOf(Resource.Success(searchResults))

        viewmodel.search("test query")
        advanceUntilIdle()

        val state = viewmodel.state.value
        assertThat(state.isSearching).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.searchResults).isEqualTo(searchResults)

        coVerify(exactly = 1) { openWeatherMapRepository.searchForCoordinatesData("test query") }
    }

    @Test
    fun searchSuccessWithEmptyResultsShowsNoResultsError() = runTest {
        coEvery {
            openWeatherMapRepository.searchForCoordinatesData(any())
        } returns flowOf(Resource.Success(emptyList()))

        viewmodel.search("test query")
        advanceUntilIdle()

        val state = viewmodel.state.value
        assertThat(state.isSearching).isFalse()
        assertThat(state.error).isEqualTo("No Results Found")
        assertThat(state.searchResults).isEmpty()

        coVerify(exactly = 1) { openWeatherMapRepository.searchForCoordinatesData("test query") }
    }

    @Test
    fun searchSuccessWithNullDataShowsNoResultsError() = runTest {
        coEvery {
            openWeatherMapRepository.searchForCoordinatesData(any())
        } returns flowOf(Resource.Success(listOf()))

        viewmodel.search("test query")
        advanceUntilIdle()

        val state = viewmodel.state.value
        assertThat(state.isSearching).isFalse()
        assertThat(state.error).isEqualTo("No Results Found")
        assertThat(state.searchResults).isEmpty()

        coVerify(exactly = 1) { openWeatherMapRepository.searchForCoordinatesData("test query") }
    }

    @Test
    fun searchErrorUpdatesStateWithErrorMessage() = runTest {
        val errorMessage = "Network error occurred"
        coEvery {
            openWeatherMapRepository.searchForCoordinatesData(any())
        } returns flowOf(Resource.Error(errorMessage))

        viewmodel.search("test query")
        advanceUntilIdle()

        val state = viewmodel.state.value
        assertThat(state.isSearching).isFalse()
        assertThat(state.error).isEqualTo(errorMessage)
        assertThat(state.searchResults).isEmpty()

        coVerify(exactly = 1) { openWeatherMapRepository.searchForCoordinatesData("test query") }
    }

    @Test
    fun currentCoordinatesExposesRepositoryFlow() {
        val coordinatesFlow = MutableStateFlow(sampleCoordinates)
        every { gpsRepository.currentCoordinates } returns coordinatesFlow

        val newViewmodel = GpsViewmodel(gpsRepository, openWeatherMapRepository)

        assertThat(newViewmodel.currentCoordinates).isEqualTo(coordinatesFlow)
    }

    @Test
    fun multipleSearchCallsHandledCorrectly() = runTest {
        val firstResults = listOf(sampleCoordinates)
        val secondResults = listOf(CoordinatesData("40.7128", "-74.0060", "New York"))

        coEvery {
            openWeatherMapRepository.searchForCoordinatesData("first")
        } returns flowOf(Resource.Success(firstResults))

        coEvery {
            openWeatherMapRepository.searchForCoordinatesData("second")
        } returns flowOf(Resource.Success(secondResults))

        viewmodel.search("first")
        advanceUntilIdle()

        assertThat(viewmodel.state.value.searchResults).isEqualTo(firstResults)

        viewmodel.search("second")
        advanceUntilIdle()

        assertThat(viewmodel.state.value.searchResults).isEqualTo(secondResults)

        coVerify(exactly = 1) { openWeatherMapRepository.searchForCoordinatesData("first") }
        coVerify(exactly = 1) { openWeatherMapRepository.searchForCoordinatesData("second") }
    }

    @Test
    fun searchClearsErrorFromPreviousSearch() = runTest {

        coEvery {
            openWeatherMapRepository.searchForCoordinatesData("error")
        } returns flowOf(Resource.Error("Some error"))

        viewmodel.search("error")
        advanceUntilIdle()

        assertThat(viewmodel.state.value.error).isEqualTo("Some error")

        coEvery {
            openWeatherMapRepository.searchForCoordinatesData("success")
        } returns flowOf(Resource.Success(listOf(sampleCoordinates)))

        viewmodel.search("success")
        advanceUntilIdle()

        val state = viewmodel.state.value
        assertThat(state.error).isNull()
        assertThat(state.searchResults).isEqualTo(listOf(sampleCoordinates))
    }
}