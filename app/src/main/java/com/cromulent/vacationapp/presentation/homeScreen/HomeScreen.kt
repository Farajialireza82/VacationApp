package com.cromulent.vacationapp.presentation.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.presentation.components.CompactLocationCardList
import com.cromulent.vacationapp.presentation.components.FullLocationCardList
import com.cromulent.vacationapp.presentation.components.SearchField
import com.cromulent.vacationapp.presentation.homeScreen.components.CategoryChip
import com.cromulent.vacationapp.presentation.homeScreen.components.HomeTopBar
import com.cromulent.vacationapp.ui.theme.NeonBlitz
import com.cromulent.vacationapp.util.Constants.CATEGORIES

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewmodel: HomeViewmodel = hiltViewModel<HomeViewmodel>(),
    openDetailsScreen: (String) -> Unit,
    openLocationPickerScreen: () -> Unit,
    openSearchScreen: () -> Unit,
) {

    val state = viewmodel.state.collectAsState()
    val currentCoordinates = viewmodel.currentCoordinates.collectAsState()
    var lastCoordinates: CoordinatesData? by rememberSaveable { mutableStateOf(null) }

    var selectedCategory by rememberSaveable { mutableStateOf(CATEGORIES[0]) }
    var snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(currentCoordinates) {
        if (currentCoordinates.value == null) {
            openLocationPickerScreen()
            return@LaunchedEffect
        } else {
            if (currentCoordinates.value != lastCoordinates) {
                viewmodel.getNearbyLocations(selectedCategory.key, clearCache = true)
                lastCoordinates = currentCoordinates.value
            }
        }
    }

    LaunchedEffect(state.value.error) {
        if (state.value.error?.isNotEmpty() == true) {
            val result = snackbarHostState.showSnackbar(
                message = state.value.error ?: "Something went wrong",
                withDismissAction = true,
                actionLabel = "Refresh",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewmodel.getNearbyLocations(selectedCategory.key)
            }
        }
    }


    Scaffold(
        modifier = modifier,
        topBar = {
            HomeTopBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(horizontal = 24.dp),
                locationText = currentCoordinates.value?.getTitle() ?: "NO TITLE",
                onRefreshClicked = {
                    viewmodel.getNearbyLocations(selectedCategory.key, clearCache = true)
                },
                onLocationClicked = {
                    openLocationPickerScreen()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        Column(
            Modifier.padding(paddingValues)
        ) {
            SearchField(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                hint = "Find things to do",
                isSearchable = false,
                onClick = openSearchScreen
            )

            Spacer(Modifier.size(18.dp))

            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(CATEGORIES) { category ->
                    CategoryChip(
                        text = category.title,
                        isSelected = selectedCategory == category
                    ) {
                        selectedCategory = category
                        viewmodel.getNearbyLocations(selectedCategory.key)
                    }
                }
            }

            Spacer(Modifier.size(32.dp))

            if ((state.value.popularLocations + state.value.recommendedLocations).isEmpty() && state.value.isLoading.not()) {

                Column(
                    Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Top
                ) {

                    Image(
                        modifier = Modifier,
                        painter = painterResource(R.drawable.empty_state),
                        contentDescription = "No results photo"
                    )

                    Spacer(Modifier.size(16.dp))


                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = NeonBlitz,
                        color = colorResource(R.color.primary),
                        text = "No results found for this location",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(Modifier.size(18.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 14.sp,
                        color = colorResource(R.color.subtitle),
                        text = "Try changing your category or location",
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                    )
                }

            } else {

                FullLocationCardList(
                    modifier = Modifier
                        .padding(start = 24.dp),
                    listTitle = "Popular",
                    locations = state.value.popularLocations,
                    isLoading = state.value.isLoading,
                    onLocationClicked = {
                        it?.locationId?.let {
                            openDetailsScreen(it)
                        }
                    },
                    onSeeAllClicked = null,
                )

                Spacer(Modifier.size(32.dp))

                CompactLocationCardList(
                    modifier = Modifier
                        .padding(start = 24.dp),
                    listTitle = "Recommended",
                    locations = state.value.recommendedLocations,
                    isLoading = state.value.isLoading,
                    onLocationClicked = {
                        it?.locationId?.let {
                            openDetailsScreen(it)
                        }
                    },
                )
            }

        }
    }

}