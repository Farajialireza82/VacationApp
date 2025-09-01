package com.cromulent.vacationapp.data.repository

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.core.app.ApplicationProvider
import com.cromulent.vacationapp.data.local.LocationCacheDao
import com.cromulent.vacationapp.data.local.LocationPhotosCacheDao
import com.cromulent.vacationapp.data.remote.OpenWeatherMapApi
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.data.remote.dto.Response
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
@ExperimentalCoroutinesApi
class GpsRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var repository: GpsRepositoryImpl
    private lateinit var application: Application
    private lateinit var openWeatherMapApi: OpenWeatherMapApi
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setup() {
        application = ApplicationProvider.getApplicationContext()
        openWeatherMapApi = mockk()
        dataStore = mockk()

        repository = GpsRepositoryImpl(application, openWeatherMapApi)


    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun searchForCoordinatesNameShouldReturnSuccessWhenApiCallSucceeds() = runTest {

        val latitude = "12.32134"
        val longitude = "12.32132"

        val responseData = createMockCoordinatesDataList()

        coEvery {
            openWeatherMapApi.searchForCoordinatesName(latitude, longitude, any(), any())
        } returns responseData

        val result = repository.searchForCoordinatesName(latitude, longitude).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data).isNotNull()
        assertThat(result.data).isEqualTo(responseData)

        coVerify { openWeatherMapApi.searchForCoordinatesName(any(), any(), any(), any()) }


    }

    @Test
    fun searchForCoordinatesNameShouldReturnErrorWhenExceptionOccurs() = runTest {

        val latitude = "12.32134"
        val longitude = "12.32132"

        val errorMessage = "Something went wrong"

        coEvery {
            openWeatherMapApi.searchForCoordinatesName(latitude, longitude, any(), any())
        } throws RuntimeException(errorMessage)

        val result = repository.searchForCoordinatesName(latitude, longitude).first()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat(result.message).isEqualTo(errorMessage)

    }

    @Test
    fun saveCurrentCoordinatesShouldUpdateStateFlow() = runTest {

        val testCoordinates = createMockCoordinateData()

        repository.saveCurrentCoordinates(testCoordinates)

        assertThat(repository.currentCoordinates.value).isEqualTo(testCoordinates)

    }

    @Test
    fun readCurrentCoordinatesShowReturnSavedCoordinates() = runTest{


        val testCoordinates = createMockCoordinateData()

        repository.saveCurrentCoordinates(testCoordinates)

        val result = repository.readCurrentCoordinates().first()

        advanceUntilIdle()

        assertThat(result).isEqualTo(testCoordinates)


    }

    private fun createMockCoordinatesDataList() = listOf(
        createMockCoordinateData(),
        createMockCoordinateData(1),
        createMockCoordinateData(2)
    )

    private fun createMockCoordinateData(
        tag: Int = 0,
        latitude: String = "12.32134",
        longitude: String = "12.32134",
    ) = CoordinatesData(
        latitude = latitude,
        longitude = longitude,
        name = "City #$tag",
        state = "State #$tag",
        country = "Country #$tag"
    )


}