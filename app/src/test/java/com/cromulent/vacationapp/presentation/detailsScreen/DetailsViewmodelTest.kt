package com.cromulent.vacationapp.presentation.detailsScreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.util.Resource
import com.cromulent.vacationapp.util.Samples
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import kotlin.math.exp

class DetailsViewmodelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewmodel: DetailsViewmodel
    private lateinit var vacationRepository: VacationRepository
    private lateinit var savedStateHandle: SavedStateHandle

    private val testDispatcher = StandardTestDispatcher()

    val sampleLocation = Samples.location

    @Before
    fun setUp() {

        Dispatchers.setMain(testDispatcher)

        vacationRepository = mockk()
        savedStateHandle = mockk()

        coEvery {
            vacationRepository.getLocationDetails(any(), any())
        } returns flowOf(Resource.Success(sampleLocation))


        every {
            savedStateHandle.get<String?>("location_id")
        } returns sampleLocation.locationId

        viewmodel = DetailsViewmodel(
            vacationRepository = vacationRepository,
            savedStateHandle = savedStateHandle
        )



    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }


    @Test
    fun getLocationDetailsDoesNotCallRepositoryWhenLocationIdIsNull() = runTest {

        clearMocks(vacationRepository)

        val mSavedStateHandle: SavedStateHandle = mockk()

        every {
            mSavedStateHandle.get<String?>(any<String>())
        } returns null

        viewmodel = DetailsViewmodel(vacationRepository, mSavedStateHandle)

        viewmodel.getLocationDetails()

        coVerify( exactly = 0 ) { vacationRepository.getLocationDetails(any(), any()) }

    }

    @Test
    fun getLocationDetailsCallsRepositoryWhenLocationIdIsNotNull() = runTest {

        viewmodel.getLocationDetails()

        coVerify(exactly = 1) { vacationRepository.getLocationDetails(sampleLocation.locationId, any()) }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getLocationDetailsUpdatesStateCorrectly() = runTest {

        viewmodel.getLocationDetails()

        advanceUntilIdle()

        assertThat(viewmodel.state.value.location).isEqualTo(sampleLocation)

    }

    @Test
    fun getLocationDetailsHandlesErrorsCorrectly() = runTest {

        val errorMessage = "Something went wrong"
        coEvery {
            vacationRepository.getLocationDetails(any())
        } returns MutableStateFlow(Resource.Error(errorMessage))

        viewmodel.getLocationDetails()

        advanceUntilIdle()

        assertThat(viewmodel.state.value.error).isEqualTo(errorMessage)

    }

    @Test
    fun getLocationDetailsForcesRefreshWhenForceRefreshIsTrue() = runTest {

        viewmodel.getLocationDetails(forceRefresh = true)

        advanceUntilIdle()

        coVerify {
            vacationRepository.getLocationDetails(any(), true)

        }

    }

}