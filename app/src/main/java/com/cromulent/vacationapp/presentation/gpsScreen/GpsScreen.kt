package com.cromulent.vacationapp.presentation.gpsScreen

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GpsFixed
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.model.CoordinatesData
import com.cromulent.vacationapp.presentation.components.SearchField
import com.cromulent.vacationapp.presentation.components.TraveloButton
import com.cromulent.vacationapp.presentation.util.TestTags
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import com.cromulent.vacationapp.util.openAppSettings
import com.cromulent.vacationapp.util.rememberLocationPermissionHandler

@Composable
fun GpsScreen(
    modifier: Modifier = Modifier,
    viewmodel: GpsViewmodel = hiltViewModel()
) {

    val state = viewmodel.state.collectAsState()
    val currentCoordinates = viewmodel.currentCoordinates.collectAsState()

    val context = LocalContext.current

    val backDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    LaunchedEffect(state.value.isCoordinateSelected) {

        if (state.value.isCoordinateSelected) {
            backDispatcher?.onBackPressed()
        }
    }

    LaunchedEffect(state.value.error) {
        if (state.value.error?.isNotEmpty() == true) {

            Toast.makeText(
                context,
                state.value.error ?: "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val requestLocationPermission = rememberLocationPermissionHandler(
        onPermissionGranted = {
            viewmodel.getCurrentLocation()
        },
        onPermissionDenied = { },
        onNeedSettings = {
            Toast.makeText(context, "GPS permission needed", Toast.LENGTH_SHORT).show()
            openAppSettings(context)
        }
    )

    Scaffold(
        modifier = modifier,
        topBar = {

            Column {

                GpsScreenTopBar(
                    modifier = Modifier
                        .background(color = colorResource(R.color.background_tertiary))
                        .padding(20.dp)
                        .windowInsetsPadding(WindowInsets.systemBars),
                    selectedLocationName = currentCoordinates.value?.getTitle()
                        ?: "No location selected yet",
                    isLocating = state.value.isLocating,
                    locateUser = {
                        requestLocationPermission()
                    }
                )

                HorizontalDivider(
                    color = colorResource(R.color.background_secondary)
                )

            }
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .background(color = Color.White)
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                SearchField(
                    hint = "Search cities, neighborhoods, landmarks..."
                ) {
                    viewmodel.search(it)
                }
            }

            HorizontalDivider(
                color = colorResource(R.color.background_secondary)
            )

            if (state.value.isSearching) {

                Spacer(Modifier.size(32.dp))

                Column (
                    Modifier
                        .testTag(TestTags.GPS_SCREEN_LOADING_STATE)
                        .fillMaxWidth()
                        .height(240.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    CircularProgressIndicator()

                    Spacer(Modifier.size(24.dp))

                    Text(
                        text = "Searching...",
                        color = colorResource(R.color.hint_color),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            } else if (state.value.searchResults.isEmpty()) {

                Box(
                    Modifier
                        .fillMaxSize()
                        .testTag(TestTags.GPS_SCREEN_START_STATE),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = if (currentCoordinates.value == null)
                            "Select your travel destination and start your journey"
                        else
                            "Search results will appear here",
                        color = colorResource(R.color.subtitle)
                    )
                }
            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(state.value.searchResults) {

                        it ?: return@items

                        CoordinatesItem(
                            modifier = Modifier,
                            coordinatesData = it
                        ) {
                            viewmodel.setCurrentCoordinates(it)
                        }

                        HorizontalDivider(
                            color = colorResource(R.color.background_secondary)
                        )

                    }
                }

            }

        }
    }


}

@Composable
fun GpsScreenTopBar(
    modifier: Modifier = Modifier,
    selectedLocationName: String,
    isLocating: Boolean = false,
    locateUser: () -> Unit
) {

    Column(modifier) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = colorResource(R.color.primary),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(10.dp),
                tint = Color.White,
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = null
            )

            Column(
                Modifier.padding(horizontal = 8.dp)
            ) {

                Text(
                    text = "Current Selection",
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp
                )

                Spacer(Modifier.size(2.dp))

                Text(
                    text = selectedLocationName,
                    fontSize = 12.sp
                )
            }

        }

        Spacer(Modifier.size(16.dp))

        TraveloButton(
            modifier = Modifier
                .testTag(TestTags.GPS_SCREEN_LOCATE_BUTTON),
            text = "Use Current Location",
            leadingIcon = Icons.Rounded.GpsFixed,
            isLoading = isLocating,
            onClick = {
                locateUser()
            }
        )


    }

}

@Composable
fun CoordinatesItem(
    modifier: Modifier = Modifier,
    coordinatesData: CoordinatesData,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 24.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Icon(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = colorResource(R.color.primary),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(10.dp),
            tint = Color.White,
            imageVector = Icons.Rounded.LocationOn,
            contentDescription = null
        )

        Column(
            Modifier.padding(horizontal = 8.dp)
        ) {

            Text(
                text = coordinatesData.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = coordinatesData.getAddressTitle(),
                fontSize = 12.sp
            )
        }

    }

}

@Preview
@Composable
private fun GpsScreenPrev() {

    VacationAppTheme {
        CoordinatesItem(

            coordinatesData = CoordinatesData(
                latitude = "22",
                longitude = "32",
                country = "Iran",
                state = "Tehran",
                name = "Varamin"
            )
        ) {}

    }


}