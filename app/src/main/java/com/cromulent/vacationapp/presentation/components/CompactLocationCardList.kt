package com.cromulent.vacationapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.common.shimmerEffect
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import com.cromulent.vacationapp.util.Samples.locationsList
import kotlin.random.Random

@Composable
fun CompactLocationCardList(
    modifier: Modifier = Modifier,
    listTitle: String,
    locations: List<Location?>,
    isLoading: Boolean = false,
    onLocationClicked: (Location?) -> Unit
) {

    Column(
        modifier = modifier
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

        }

        LazyRow(
            Modifier.padding(top = 12.dp)
        ) {

            if(isLoading){

                items(5) {
                    Box(
                        modifier = Modifier
                            .width(188.dp)
                            .height(153.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    ) {}

                    Spacer(Modifier.size(24.dp))

                }


            } else {

            items(locations) { location ->
                CompactLocationCard(
                    location = location,
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
private fun CompLocListPrev() {

    val locations = locationsList

    VacationAppTheme {

        CompactLocationCardList(
            listTitle = "Recommended",
            locations = locations,
            isLoading = true,
            onLocationClicked = {}
        )

    }

}