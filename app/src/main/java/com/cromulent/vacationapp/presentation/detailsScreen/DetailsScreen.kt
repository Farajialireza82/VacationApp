package com.cromulent.vacationapp.presentation.detailsScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.common.shimmerEffect
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.presentation.detailsScreen.components.AmenitiesList
import com.cromulent.vacationapp.presentation.onBoardingScreen.components.OnBoardingButton
import com.cromulent.vacationapp.ui.theme.ColorPrimary
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import androidx.core.net.toUri
import com.cromulent.vacationapp.model.LocationPhoto
import com.cromulent.vacationapp.presentation.components.LocationPhotoSlider
import com.cromulent.vacationapp.presentation.detailsScreen.components.AmenitiesBottomSheet
import com.cromulent.vacationapp.util.openMapWithLocation
import com.cromulent.vacationapp.util.openWebsite

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewmodel: DetailsViewmodel,
) {

    val backDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val state = viewmodel.state.collectAsState()

    var isAmenitiesBottomSheetVisible by remember { mutableStateOf(false) }

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
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.white))
            ) {

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "Price Figure",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        modifier = Modifier
                            .wrapContentWidth(),
                        text = state.value.location?.priceLevel ?: "Free",
                        color = colorResource(R.color.money_color),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OnBoardingButton(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                        .weight(2f)
                        .shadow(
                            elevation = 8.dp,
                            spotColor = colorResource(R.color.primary),
                            ambientColor = colorResource(R.color.primary)
                        )
                        .clip(RoundedCornerShape(16.dp)),
                    text = "Visit Website",
                    icon = Icons.AutoMirrored.Default.ArrowForward,
                ) {
                    openWebsite(
                        context = context,
                        url = state.value.location?.webUrl
                    )
                }


            }
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
                )

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
                    text = "${state.value.location?.rating ?: "No ratings"} (${state.value.location?.reviewCount?: "0"} reviews)",
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

    }
}

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 4,
    fontSize: TextUnit = 14.sp,
    lineHeight: TextUnit = 20.sp,
    color: Color = colorResource(R.color.subtitle_secondary)
) {
    var isExpanded by remember { mutableStateOf(false) }
    var hasOverflow by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        modifier = modifier
            .animateContentSize(),
        text = text,
        maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
        overflow = TextOverflow.Ellipsis,
        lineHeight = lineHeight,
        color = color,
        fontSize = fontSize,
        onTextLayout = { layoutResult ->
            textLayoutResult = layoutResult
            hasOverflow = layoutResult.hasVisualOverflow
        }
    )

    // Show expand/collapse button only if text overflows
    if (hasOverflow || isExpanded) {
        Spacer(Modifier.size(8.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clickable {
                    isExpanded = !isExpanded
                }
        ) {
            Text(
                fontWeight = FontWeight.W600,
                text = if (isExpanded) "Read Less" else "Read More",
                fontSize = 12.sp,
                color = colorResource(R.color.primary)
            )

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                tint = ColorPrimary,
                contentDescription = null
            )
        }
    }
}
