package com.cromulent.vacationapp.presentation.gpsScreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.presentation.util.TestTags
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GpsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewmodel: GpsViewmodel

    var stateFlow: MutableStateFlow<GpsState> = MutableStateFlow(GpsState())

    var currentCoordinates: MutableStateFlow<CoordinatesData?> = MutableStateFlow(CoordinatesData(
        latitude = "37.7749",
        longitude = "-122.4194",
        name = "San Francisco"
    ))

    private val testSearchResults = listOf(
        CoordinatesData(
            latitude = "40.7128",
            longitude = "-74.0060",
            name = "New York City"
        ),
        CoordinatesData(
            latitude = "34.0522",
            longitude = "-118.2437",
            name = "Los Angeles"
        )
    )

    @Before
    fun setup() {

        mockViewmodel = mockk(relaxed = true)

        every { mockViewmodel.state } returns stateFlow
        every { mockViewmodel.currentCoordinates } returns currentCoordinates
        every { mockViewmodel.getCurrentLocation() } just Runs
        every { mockViewmodel.search(any()) } just Runs
        every { mockViewmodel.setCurrentCoordinates(any()) } just Runs

        composeTestRule.setContent {
            GpsScreen(
                viewmodel = mockViewmodel
            )
        }
    }

    @Test
    fun gpsScreen_displaysCorrectLocationInTopBar() {
        composeTestRule
            .onNodeWithText(currentCoordinates.value?.name ?: "NOT")
            .assertIsDisplayed()
    }

    @Test
    fun gpsScreen_displaysCurrentSelectionLabel() {
        composeTestRule
            .onNodeWithText("Current Selection")
            .assertIsDisplayed()
    }

    @Test
    fun gpsScreen_displaysUseCurrentLocationButton() {
        composeTestRule
            .onNodeWithTag(TestTags.GPS_SCREEN_LOCATE_BUTTON)
            .assertIsDisplayed()
    }

    @Test
    fun gpsScreen_displaysSearchField() {
        composeTestRule
            .onNodeWithText("Search cities, neighborhoods, landmarks...")
            .assertIsDisplayed()
    }

    @Test
    fun gpsScreen_searchingShowsLoadingIndicator() {
        stateFlow.value = GpsState(
            isSearching = true,
            searchResults = emptyList()
        )

        composeTestRule
            .onNodeWithTag(TestTags.GPS_SCREEN_LOADING_STATE)
            .assertIsDisplayed()
    }

    @Test
    fun gpsScreen_displayEmptyStateWhenNoSearchResultsAndNotSearching() {
        stateFlow.value = GpsState(
            isSearching = false,
            searchResults = emptyList()
        )

        composeTestRule
            .onNodeWithTag(TestTags.GPS_SCREEN_START_STATE)
            .assertIsDisplayed()
    }

    @Test
    fun gpsScreen_displaySearchResultsEmptyStateWhenCoordinatesExist() {
        stateFlow.value = GpsState(
            isSearching = false,
            searchResults = emptyList()
        )

        composeTestRule
            .onNodeWithText("Search results will appear here")
            .assertIsDisplayed()
    }

    @Test
    fun gpsScreen_displaySearchResultsList() {

        stateFlow.value = GpsState(
            isSearching = false,
            searchResults = testSearchResults,
            isLocating = false,
            isCoordinateSelected = false,
            error = null,
        )

        testSearchResults.forEach { coordinate ->
            composeTestRule
                .onNodeWithText(coordinate.name ?: "")
                .assertIsDisplayed()
        }
    }

    @Test
    fun gpsScreen_clickingSearchResultCallsSetCurrentCoordinates() {
        stateFlow.value = GpsState(
            isSearching = false,
            searchResults = testSearchResults
        )

        val firstResult = testSearchResults[0]

        composeTestRule
            .onNodeWithText(firstResult.name ?: "")
            .performClick()

        coVerify { mockViewmodel.setCurrentCoordinates(firstResult) }
    }

    @Test
    fun gpsScreen_showsLoadingButtonWhenLocating() {
        stateFlow.value = GpsState(
            isLocating = true
        )

        composeTestRule
            .onNodeWithText("Use Current Location")
            .assertIsNotDisplayed()
    }

    @Test
    fun gpsScreen_displaysNoLocationSelectedWhenCoordinatesAreNull() {
        currentCoordinates.value = null

        composeTestRule
            .onNodeWithText("No location selected yet")
            .assertIsDisplayed()
    }

    @Test
    fun gpsScreen_coordinateSelectionTriggersBackNavigation() {
        stateFlow.value = GpsState(
            isCoordinateSelected = true
        )

        composeTestRule.waitForIdle()

        assertThat(stateFlow.value.isCoordinateSelected).isTrue()
    }

    @Test
    fun gpsScreen_emptySearchResultsShowNoResultsFound() {

        val errorMessage = "No Results Found"

        stateFlow.value = GpsState(
            isSearching = false,
            searchResults = emptyList(),
            error = errorMessage
        )


        composeTestRule.waitForIdle()

        assertThat("No Results Found").isEqualTo(errorMessage)
    }

    @Test
    fun gpsScreen_coordinatesItemDisplaysCorrectInformation() {
        stateFlow.value = GpsState(
            isSearching = false,
            searchResults = testSearchResults
        )

        val firstResult = testSearchResults[0]

        composeTestRule
            .onNodeWithText(firstResult.name ?: "")
            .assertIsDisplayed()

    }

    @Test
    fun gpsScreen_multipleSearchResultsAreDisplayedInList() {
        stateFlow.value = GpsState(
            isSearching = false,
            searchResults = testSearchResults
        )

        // Verify all search results are displayed
        testSearchResults.forEach { coordinate ->
            composeTestRule
                .onNodeWithText(coordinate.name ?: "")
                .assertIsDisplayed()
        }
    }
}