package com.cromulent.vacationapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cromulent.vacationapp.data.local.LocationCacheDao
import com.cromulent.vacationapp.data.local.LocationPhotosCacheDao
import com.cromulent.vacationapp.data.model.LocationPhotoListEntity
import com.cromulent.vacationapp.data.remote.OpenWeatherMapApi
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.data.remote.dto.Response
import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.CoordinatesData
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

class OpenWeatherMapRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: OpenWeatherMapRepositoryImpl
    private lateinit var openWeatherMapApi: OpenWeatherMapApi

    @Before
    fun setup() {
        openWeatherMapApi = mockk()

        repository = OpenWeatherMapRepositoryImpl(
            openWeatherMapApi = openWeatherMapApi
        )

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun getWeatherShouldReturnSuccessWhenApiCallSucceeds() = runTest {

        val response = createMockCoordinatesDataList()

        coEvery {
            openWeatherMapApi.searchForCoordinatesData(any(), any(), any())
        } returns response

        val result = repository.searchForCoordinatesData("query", 5).first()

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data).isNotNull()
        assertThat(result.data).isEqualTo(response)

        coVerify(exactly = 1) {
            openWeatherMapApi.searchForCoordinatesData(any(), any(), any())
        }


    }

    @Test
    fun getWeatherShouldReturnErrorWhenExceptionOccurs() = runTest {

        val errorMessage = "Something went wrong"

        coEvery {
            openWeatherMapApi.searchForCoordinatesData(any(), any(), any())
        } throws RuntimeException(errorMessage)


        val result = repository.searchForCoordinatesData("query", 5).first()

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat(result.message).isEqualTo(errorMessage)

        coVerify(exactly = 1) {
            openWeatherMapApi.searchForCoordinatesData(any(), any(), any())
        }



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