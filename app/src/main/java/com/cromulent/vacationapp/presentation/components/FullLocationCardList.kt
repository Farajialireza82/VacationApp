package com.cromulent.vacationapp.presentation.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.common.shimmerEffect
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.presentation.util.TestTags
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import com.cromulent.vacationapp.util.Samples.locationsList
import kotlin.random.Random

@Composable
fun FullLocationCardList(
    modifier: Modifier = Modifier,
    listTitle: String,
    locations: List<Location?>,
    isLoading: Boolean = false,
    onLocationClicked: (String) -> Unit,
    onSeeAllClicked: (() -> Unit)? = null,
) {

    Column(
        modifier = modifier
            .testTag(TestTags.FULL_LOCATIONS_LIST)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = listTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.W600
            )

            onSeeAllClicked?.let {

                Text(
                    modifier = Modifier
                        .clickable(
                            onClick = onSeeAllClicked
                        ),
                    fontWeight = FontWeight.W400,
                    text = "See all",
                    fontSize = 12.sp,
                    color = colorResource(R.color.primary)
                )
            }

        }

        LazyRow(
            Modifier.padding(top = 12.dp)
        ) {

            if (isLoading) {

                items(5) {
                    Box(
                        modifier = Modifier
                            .width(188.dp)
                            .height(240.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .shimmerEffect()
                    )
                    Spacer(Modifier.size(24.dp))
                }

            } else {
                items(locations) { location ->
                    FullLocationCard(
                        location = location,
                        isBookmarked = false,
                        onClick = onLocationClicked
                    )
                    Spacer(Modifier.size(24.dp))
                }
            }


        }
    }
}

@Preview
@Composable
private fun FullLocPrev() {

    val locations = locationsList

    VacationAppTheme {

        FullLocationCardList(
            listTitle = "Popular",
            locations = locations,
            isLoading = true,
            onLocationClicked = {},
        )

    }

}