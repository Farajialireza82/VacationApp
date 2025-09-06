package com.cromulent.vacationapp.presentation.detailsScreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cromulent.vacationapp.presentation.searchScreen.SearchScreen
import com.cromulent.vacationapp.presentation.searchScreen.SearchState
import com.cromulent.vacationapp.presentation.util.TestTags
import com.cromulent.vacationapp.util.Samples
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.intellij.lang.annotations.Flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var mockViewmodel: DetailsViewmodel
    lateinit var stateFlow: MutableStateFlow<DetailsState>

    var location = Samples.location


    @Before
    fun setup(){
        mockViewmodel = mockk(relaxed = true)

        stateFlow = MutableStateFlow(DetailsState(
            location = location,
            isLoading = false,
            error = null
        ))

        every { mockViewmodel.state } returns stateFlow

        composeTestRule.setContent {
            DetailsScreen(
                viewmodel = mockViewmodel
            )
        }
    }

    @After
    fun teardown(){
        clearAllMocks()
    }

    @Test
    fun detailsScreen_LoadingStateWorksCorrectly(){

        stateFlow.value = DetailsState(
            location = null,
            isLoading = true,
            error = null
        )

        composeTestRule
            .onNodeWithTag(
                TestTags.DETAILS_SCREEN_LOADING
            ).assertIsDisplayed()

    }

    @Test
    fun detailsScreen_emptyStateDisplaysCorrectly() = runTest{

        stateFlow.value = DetailsState(
            location = null,
            isLoading = false,
            error = null
        )

        composeTestRule
            .onNodeWithTag(TestTags.DETAILS_SCREEN_EMPTY_STATE).assertIsDisplayed()

    }

    @Test
    fun detailsScreen_BackButtonIsDisplayed(){

        composeTestRule
            .onNodeWithTag(TestTags.BACK_BUTTON)
            .assertIsDisplayed()

    }

    @Test
    fun detailsScreen_LocationNameIsDisplayedCorrectly(){

        composeTestRule
            .onNodeWithText(location.name)
            .assertIsDisplayed()

    }

    @Test
    fun detailsScreen_LocationRatingIsDisplayed(){

        composeTestRule
            .onNodeWithText("${location.rating ?: "No ratings"} (${location.reviewCount ?: "0"} reviews)")
            .assertIsDisplayed()
    }

    @Test
    fun detailsScreen_LocationDetailsIsDisplayedCorrectly(){

            composeTestRule
                .onNodeWithText(
                    location.description!!
                ).assertIsDisplayed()
    }

    @Test
    fun detailsScreen_LocationDetailsEmptyStateIsDisplayedCorrectly(){

        val mLocation = location.copy(
            description = null
        )

        stateFlow.value = DetailsState(
            isLoading = false,
            location = mLocation,
            error = null
        )

        composeTestRule
            .onNodeWithText("No description included")
            .assertIsDisplayed()

    }

    @Test
    fun detailsScreen_AmenitiesBottomSheetOpensCorrectly(){

        composeTestRule
            .onNodeWithText("See All")
            .performClick()

        composeTestRule
            .onNodeWithTag(TestTags.AMENITIES_BOTTOM_SHEET)
            .assertIsDisplayed()

    }

    @Test
    fun detailsScreen_IsPriceFigureDisplayedCorrectly(){

        composeTestRule
            .onNodeWithText(location.priceLevel!!)
            .assertIsDisplayed()

        val mLocation = location.copy(
            priceLevel = null
        )

        stateFlow.value = DetailsState(
            isLoading = false,
            location = mLocation,
            error = null
        )


        composeTestRule
            .onNodeWithText("Unknown")
            .assertIsDisplayed()

    }

}