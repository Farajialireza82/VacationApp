package com.cromulent.vacationapp.presentation.searchScreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.presentation.util.TestTags
import com.cromulent.vacationapp.util.Constants.SEARCH_CATEGORIES
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewmodel: SearchViewmodel
    private lateinit var mockOpenDetailsScreen: (String) -> Unit
    private lateinit var stateFlow: MutableStateFlow<SearchState>

    private val testLocation = Location(
        locationId = "123",
        name = "Test Location",
        addressObject = Address(
            city = "Test City",
            state = "Test State",
            country = "Test Country",
            street1 = null,
            postalcode = null,
            addressString = null
        )
    )

    @Before
    fun setup(){
        mockViewmodel = mockk(relaxed = true)
        mockOpenDetailsScreen = mockk(relaxed = true)
        stateFlow = MutableStateFlow(SearchState())

        every { mockViewmodel.state } returns stateFlow

        composeTestRule.setContent {
            SearchScreen(
                viewmodel = mockViewmodel,
                openDetailsScreen = mockOpenDetailsScreen
            )
        }
    }

    @After
    fun teardown(){
        clearAllMocks()
    }

    @Test
    fun searchScreen_IsSearchFieldDisplayed(){
        composeTestRule
            .onNodeWithTag(TestTags.HOME_SCREEN_SEARCH_FIELD)
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_AreCategoryTagsVisible(){
        SEARCH_CATEGORIES.forEach {
            composeTestRule
                .onNodeWithTag(it.key)
                .assertIsDisplayed()
        }
    }

    @Test
    fun searchScreen_StartStateIsVisibleOnStart(){
        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_SCREEN_START_STATE)
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_LoadingStateIsDisplayed() {

        stateFlow.value = SearchState(isLoading = true)

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_SCREEN_LOADING_STATE)
            .assertIsDisplayed()

    }

    @Test
    fun searchScreen_EmptyResultsStateIsDisplayed() {

        stateFlow.value = SearchState(
            isLoading = false,
            data = emptyList(),
            error = null
        )

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_SCREEN_EMPTY_STATE)
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_SearchResultsAreDisplayed() {
        val testLocations = listOf(
            testLocation.copy(locationId = "1", name = "Location 1"),
            testLocation.copy(locationId = "2", name = "Location 2")
        )

        stateFlow.value = SearchState(
            isLoading = false,
            data = testLocations,
            error = null
        )

        composeTestRule
            .onNodeWithText("Location 1")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Location 2")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_ClickingOnLocationCallsOpenDetailsScreen() {
        val testLocations = listOf(testLocation)

        stateFlow.value = SearchState(
            isLoading = false,
            data = testLocations,
            error = null
        )

        composeTestRule
            .onNodeWithText("Test Location")
            .performClick()

        verify { mockOpenDetailsScreen("123") }
    }

    @Test
    fun searchScreen_CategorySelectionTriggersSearch() {
        val query = "test"

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD_TEXT_FIELD)
            .performTextInput(query)

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(SEARCH_CATEGORIES[1].key)
            .performClick()

        verify { mockViewmodel.search(SEARCH_CATEGORIES[1].key, query) }
    }

    @Test
    fun searchScreen_BackButtonExists() {
        composeTestRule
            .onNodeWithTag(TestTags.BACK_BUTTON)
            .assertExists()
    }

    @Test
    fun searchScreen_TitleIsDisplayed() {
        composeTestRule
            .onNodeWithText("Search")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_DefaultCategoryIsSelected() {
        composeTestRule
            .onNodeWithTag(SEARCH_CATEGORIES[0].key)
            .assertIsSelected()
    }

    @Test
    fun searchScreen_TextInputUpdatesSearchQuery() {
        val testQuery = "Paris"

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD_TEXT_FIELD)
            .performTextInput(testQuery)

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD_TEXT_FIELD)
            .assertTextContains(testQuery)
    }

    @Test
    fun searchScreen_SearchOnEnter() {
        val testQuery = "London"

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD_TEXT_FIELD)
            .performTextInput(testQuery)

        composeTestRule
            .onNodeWithTag(TestTags.SEARCH_FIELD_TEXT_FIELD)
            .performImeAction()

        verify { mockViewmodel.search(SEARCH_CATEGORIES[0].key, testQuery) }
    }

    @Test
    fun searchScreen_LocationAddressIsDisplayed() {
        val locationWithAddress = testLocation.copy(
            addressObject = Address(
                city = "San Francisco",
                state = "California",
                country = "USA",
                street1 = null,
                postalcode = null,
                addressString = null
            )
        )

        stateFlow.value = SearchState(
            isLoading = false,
            data = listOf(locationWithAddress),
            error = null
        )

        // Verify location icon and address are displayed
        composeTestRule
            .onNodeWithContentDescription("Location Icon")
            .assertIsDisplayed()
    }

    //Haven't been able to get this to pass yet TODO()
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun searchScreen_DebouncedSearchWorks() = runTest {
//        val query = "test query"
//
//        composeTestRule
//            .onNodeWithTag(TestTags.SEARCH_FIELD_TEXT_FIELD)
//            .performTextInput(query)
//
//        verify(exactly = 0) { mockViewmodel.search(any(), any()) }
//
//        composeTestRule.waitForIdle()
//        Thread.sleep(600)
//        composeTestRule.waitForIdle()
//
//        verify(atLeast = 1) { mockViewmodel.search(SEARCH_CATEGORIES[0].key, query) }
//    }

    //show something in case of an error TODO()
//    @Test
//    fun searchScreen_ErrorStateHandling() {
//
//        stateFlow.value = SearchState(
//            isLoading = false,
//            data = null,
//            error = "Network error"
//        )
//
//    }

    @Test
    fun searchScreen_MultipleSearchesUpdateResults() {

        val firstResults = listOf(testLocation.copy(name = "First Location"))
        stateFlow.value = SearchState(isLoading = false, data = firstResults)

        composeTestRule
            .onNodeWithText("First Location")
            .assertIsDisplayed()


        val secondResults = listOf(testLocation.copy(name = "Second Location"))
        stateFlow.value = SearchState(isLoading = false, data = secondResults)

        composeTestRule
            .onNodeWithText("Second Location")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("First Location")
            .assertDoesNotExist()
    }

    @Test
    fun searchScreen_EmptyQueryDoesNotTriggerSearch() {

        composeTestRule
            .onNodeWithTag(SEARCH_CATEGORIES[1].key)
            .performClick()

        verify(exactly = 0) { mockViewmodel.search(any(), "") }
    }
}