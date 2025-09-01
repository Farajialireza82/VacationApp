package com.cromulent.vacationapp.presentation.homeScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.model.ImageData
import com.cromulent.vacationapp.model.Images
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.presentation.util.TestTags
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import com.cromulent.vacationapp.util.Constants.CATEGORIES
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewmodel: HomeViewmodel
    private lateinit var mockOpenDetailsScreen: (String) -> Unit
    private lateinit var mockOpenLocationPickerScreen: () -> Unit
    private lateinit var mockOpenSearchScreen: () -> Unit

    private val testCoordinates = CoordinatesData(
        latitude = "37.7749",
        longitude = "-122.4194",
        name = "San Francisco"
    )

    @Before
    fun setup(){
        mockViewmodel = mockk(relaxed = true)
        mockOpenDetailsScreen = mockk(relaxed = true)
        mockOpenLocationPickerScreen = mockk(relaxed = true)
        mockOpenSearchScreen = mockk(relaxed = true)

        every { mockViewmodel.state } returns MutableStateFlow(HomeState())
        every { mockViewmodel.currentCoordinates } returns MutableStateFlow(testCoordinates)
        every { mockViewmodel.getNearbyLocations(any(), any()) } just Runs

    }



    @Test
    fun homeScreen_displaysCorrectLocationInTopBar() {

        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        composeTestRule
            .onNodeWithText("San Francisco")
            .assertIsDisplayed()

    }

    @Test
    fun homeScreen_displaySearchField() {
        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displayCategoryChips() {
        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        CATEGORIES.forEach {
            composeTestRule
                .onNodeWithText(it.title)
                .assertIsDisplayed()
        }

    }

    @Test
    fun homeScreen_clickingCategoryChipCallsGetNearbyLocations(){

        composeTestRule.setContent {
                HomeScreen(
                    viewmodel = mockViewmodel,
                    openDetailsScreen = mockOpenDetailsScreen,
                    openLocationPickerScreen = mockOpenLocationPickerScreen,
                    openSearchScreen = mockOpenSearchScreen
                )
        }

        composeTestRule
            .onNodeWithText(CATEGORIES[0].title)
            .performClick()

        coVerify {
            mockViewmodel.getNearbyLocations(CATEGORIES[0].key, any())
        }
    }

    @Test
    fun homeScreen_clickingSearchFieldOpensSearchScreen(){

        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD)
            .performClick()

        verify { mockOpenSearchScreen() }

    }

    @Test
    fun homeScreen_displayEmptyStateWhenNoLocationsAndNotLoading() {

        val emptyState = HomeState(
            popularLocations = emptyList(),
            recommendedLocations = emptyList(),
            isLoading = false,
            error = null
        )

        every {
            mockViewmodel.state
        } returns MutableStateFlow(emptyState)

        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

            composeTestRule
                .onNodeWithTag(TestTags.EMPTY_STATE)
                .assertIsDisplayed()

    }

    @Test
    fun homeScreen_displayLocationListWhenDataIsAvailable(){


        val mockState = HomeState(
            popularLocations = createMockLocations(),
            recommendedLocations = createMockLocations(),
            isLoading = false,
            error = null
        )

        every {
            mockViewmodel.state
        } returns MutableStateFlow(mockState)

        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }


        composeTestRule
            .onNodeWithTag(TestTags.FULL_LOCATIONS_LIST)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(TestTags.COMPACT_LOCATIONS_LIST)
            .assertIsDisplayed()

    }

    @Test
    fun homeScreen_showSnackbarWhenErrorOccurs(){

        val errorMessage = "Something went wrong"
        val mockState = HomeState(
            popularLocations = emptyList(),
            recommendedLocations = emptyList(),
            isLoading = false,
            error = errorMessage
        )

        every { mockViewmodel.state } returns MutableStateFlow(mockState)

        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        composeTestRule
            .onNodeWithText(errorMessage)
            .assertIsDisplayed()

    }

    @Test
    fun homeScreen_refreshButtonCallsGetNearbyLocationWithClearCache(){

        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.APP_LOGO)
            .performClick()

        coVerify {
            mockViewmodel.getNearbyLocations(any(), true)
        }

    }

    @Test
    fun homeScreen_openLocationPickerWhenCoordinatesAreNull(){

        every { mockViewmodel.currentCoordinates } returns MutableStateFlow(null)

        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        coVerify {
            mockOpenLocationPickerScreen()
        }

    }

    @Test
    fun homeScreen_callsGetNearbyLocationsWhenCoordinatesChange(){

        val coordinatesFlow = MutableStateFlow(testCoordinates)
        every { mockViewmodel.currentCoordinates } returns coordinatesFlow

        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        coordinatesFlow.value = CoordinatesData("123.12", "123,23", "NYC")

        verify { mockViewmodel.getNearbyLocations(any(), clearCache = true) }

    }


    @Test
    fun homeScreen_snackbarRefreshActionButtonCallsGetNearbyLocations(){

        val errorState = HomeState(
            popularLocations = emptyList(),
            recommendedLocations = emptyList(),
            isLoading = false,
            error = "errorMessage"
        )

        every { mockViewmodel.state } returns MutableStateFlow(errorState)


        composeTestRule.setContent {
            HomeScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen,
                openLocationPickerScreen = mockOpenLocationPickerScreen,
                openSearchScreen = mockOpenSearchScreen
            )
        }

        composeTestRule
            .onNodeWithText("Refresh")
            .performClick()

        verify {
            mockViewmodel.getNearbyLocations(any(), any())
        }


    }


    // Helper methods
    private fun createMockLocations(): List<Location> {
        return listOf(
            Location(
                locationId = "1",
                name = "Test Restaurant 1",
                distance = "0.5",
                description = "Great food and atmosphere",
                reviewCount = "150",
                rating = "4.5",
                latitude = "37.7749",
                longitude = "-122.4194",
                seeAllPhotosLink = "https://example.com/photos1",
                priceLevel = "$$$",
                locationPhotos = createMockLocationPhotos(),
                amenities = listOf("WiFi", "Outdoor Seating"),
                addressObject = Address(
                    street1 = "123 Food St",
                    city = "San Francisco",
                    state = "CA",
                    country = "US",
                    postalcode = "94105",
                    addressString = "123 Food St, San Francisco, CA 94105"
                )
            ),
            Location(
                locationId = "2",
                name = "Test Restaurant 2",
                distance = "1.2",
                description = "Excellent service and quality",
                reviewCount = "300",
                rating = "4.2",
                latitude = "37.7849",
                longitude = "-122.4094",
                seeAllPhotosLink = "https://example.com/photos2",
                priceLevel = "$$",
                locationPhotos = createMockLocationPhotos(),
                amenities = listOf("Parking", "Delivery"),
                addressObject = Address(
                    street1 = "456 Dining Ave",
                    city = "San Francisco",
                    state = "CA",
                    country = "US",
                    postalcode = "94106",
                    addressString = "456 Dining Ave, San Francisco, CA 94106"
                )
            )
        )
    }

    private fun createMockLocationPhotos(): List<LocationPhoto> {
        return listOf(
            LocationPhoto(
                id = 1L,
                caption = "Beautiful restaurant interior",
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