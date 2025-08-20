package com.cromulent.vacationapp.presentation.detailsScreen

import android.annotation.SuppressLint
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.presentation.components.LocationPhotoSlider
import com.cromulent.vacationapp.presentation.components.LocationPhotosBottomSheet
import com.cromulent.vacationapp.presentation.components.PhotoCard
import com.cromulent.vacationapp.presentation.detailsScreen.components.AmenitiesBottomSheet
import com.cromulent.vacationapp.presentation.detailsScreen.components.AmenitiesList
import com.cromulent.vacationapp.presentation.detailsScreen.components.DetailsBottomBar
import com.cromulent.vacationapp.presentation.detailsScreen.components.ExpandableText
import com.cromulent.vacationapp.presentation.onBoardingScreen.components.OnBoardingButton
import com.cromulent.vacationapp.ui.theme.ColorPrimary
import com.cromulent.vacationapp.util.openMapWithLocation
import com.cromulent.vacationapp.util.openWebsite
import kotlinx.coroutines.launch

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewmodel: DetailsViewmodel,
) {

    val backDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val state = viewmodel.state.collectAsState()

    var isAmenitiesBottomSheetVisible by remember { mutableStateOf(false) }


    var fullScreenPhotoIndex by remember { mutableStateOf<Int>(0) }
    var isPhotoFullScreen by remember { mutableStateOf(false) }

    val context = LocalContext.current

    var isDescriptionExpanded by remember { mutableStateOf(false) }

    if (state.value.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(80.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            DetailsBottomBar(
                priceRange = state.value.location?.priceLevel,
                openWebsite = {
                    openWebsite(
                        context = context,
                        url = state.value.location?.webUrl
                    )
                }
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(it),
        ) {

            Box(
                modifier = Modifier
                    .padding(bottom = 32.dp, top = 20.dp, start = 20.dp, end = 20.dp),
                contentAlignment = Alignment.Center
            ) {


                LocationPhotoSlider(
                    modifier = Modifier
                        .height(350.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp)),
                    photos = state.value.location?.locationPhotos
                ) {
                    fullScreenPhotoIndex = state.value.location?.locationPhotos?.indexOf(it)?: 0
                    isPhotoFullScreen = true
                }

                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(50.dp)
                        .background(
                            color = colorResource(R.color.background_secondary),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape((8.dp)))
                        .clickable {
                            backDispatcher?.onBackPressed()
                        }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .fillMaxSize()
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_left),
                        tint = colorResource(R.color.subtitle),
                        contentDescription = null
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = state.value.location?.name ?: "Location Name",
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )


                Text(
                    modifier = Modifier
                        .clickable {
                            openMapWithLocation(
                                context = context,
                                latitude = state.value.location?.latitude,
                                longitude = state.value.location?.longitude,
                                locationName = state.value.location?.name
                            )
                        },
                    fontWeight = FontWeight.W600,
                    text = "Show map",
                    fontSize = 12.sp,
                    color = colorResource(R.color.primary)
                )

            }

            Row(
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        start = 20.dp,
                        end = 20.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = null
                )

                Spacer(Modifier.size(4.dp))

                Text(
                    text = "${state.value.location?.rating ?: "No ratings"} (${state.value.location?.reviewCount ?: "0"} reviews)",
                    fontSize = 12.sp,
                    color = colorResource(R.color.subtitle_secondary)
                )
            }



            Spacer(Modifier.size(16.dp))


            val locationDescription = state.value.location?.description ?: "No description included"

            ExpandableText(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = locationDescription,
                maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 4,
                lineHeight = 20.sp,
                color = colorResource(R.color.subtitle_secondary),
                fontSize = 14.sp
            )

            Spacer(Modifier.size(32.dp))


            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                text = "Amenities",
                fontWeight = FontWeight.W500,
                fontSize = 18.sp
            )


            if (state.value.location?.amenities?.isNotEmpty() == true) {

                Spacer(Modifier.size(16.dp))

                AmenitiesList(
                    amenities = state.value.location?.amenities,
                    onSeeAllClicked = { isAmenitiesBottomSheetVisible = true }
                )
            } else {

                Spacer(Modifier.size(8.dp))

                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .animateContentSize(),
                    text = "No amenities included",
                    color = colorResource(R.color.subtitle_secondary),
                    fontSize = 14.sp,
                )
            }
        }

        if (isAmenitiesBottomSheetVisible) {
            AmenitiesBottomSheet(
                amenities = state.value.location?.amenities ?: listOf(),
                onDismiss = { isAmenitiesBottomSheetVisible = false }
            )
        }

        if(isPhotoFullScreen) {

            LocationPhotosBottomSheet(
                locationPhotos = state.value.location?.locationPhotos,
                initialPhotoIndex = fullScreenPhotoIndex,
                onDismiss = { isPhotoFullScreen = false },
            )
        }

    }
}
