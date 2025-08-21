package com.cromulent.vacationapp.presentation.homeScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cromulent.vacationapp.presentation.components.CompactLocationCardList
import com.cromulent.vacationapp.presentation.components.FullLocationCardList
import com.cromulent.vacationapp.presentation.components.SearchField
import com.cromulent.vacationapp.presentation.homeScreen.components.CategoryChip
import com.cromulent.vacationapp.presentation.homeScreen.components.HomeTopBar
import com.cromulent.vacationapp.util.Constants.CATEGORIES

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewmodel: HomeViewmodel,
    openDetailsScreen: (String) -> Unit,
    openLocationPickerScreen: () -> Unit
) {

    val state = viewmodel.state.collectAsState()
    val currentCoordinates = viewmodel.currentCoordinates.collectAsState()
    var selectedCategory by rememberSaveable { mutableStateOf(CATEGORIES[0]) }
    var snackbarHostState  = remember{ SnackbarHostState() }

    LaunchedEffect(selectedCategory) {
//        viewmodel.getNearbyLocations(selectedCategory.key)
    }

    LaunchedEffect(currentCoordinates.value) {
        if(currentCoordinates.value == null){
            openLocationPickerScreen()
            return@LaunchedEffect
        }
    }

    LaunchedEffect(state.value.error) {
        if (state.value.error?.isNotEmpty() == true) {
           val result =  snackbarHostState.showSnackbar(
                message = state.value.error ?: "Something went wrong",
                withDismissAction = true,
                actionLabel = "Refresh",
               duration =  SnackbarDuration.Short)
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
                    viewmodel.generateError()
//                    viewmodel.clearCachedLocations()
//                    viewmodel.getNearbyLocations(selectedCategory.key)
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
                hint = "Find things to do"
            ){

            }

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
                    }
                }
            }

            Spacer(Modifier.size(32.dp))

            FullLocationCardList(
                modifier = Modifier
                    .padding(start = 24.dp),
                listTitle = "Popular",
                locations = state.value.locations,
                isLoading = state.value.isLoading,
                onLocationClicked = {
                    it?.locationId?.let {
                        openDetailsScreen(it)
                    }
                },
                onSeeAllClicked = {},
            )

            Spacer(Modifier.size(32.dp))

            CompactLocationCardList(
                modifier = Modifier
                    .padding(start = 24.dp),
                listTitle = "Recommended",
                locations = state.value.locations,
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