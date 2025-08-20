package com.cromulent.vacationapp.presentation.gpsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GpsFixed
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.presentation.components.SearchField
import com.cromulent.vacationapp.presentation.onBoardingScreen.components.OnBoardingButton

@Composable
fun GpsScreen(
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars),
        topBar = {

            Column {

                GpsScreenTopBar(
                    modifier = Modifier
                        .background(color = colorResource(R.color.background_tertiary))
                        .padding(20.dp)
                ) { }

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
        ){

            Box(
                modifier = Modifier
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ){
                SearchField(
                    hint = "Search cities, neighborhoods, landmarks..."
                )
            }

            HorizontalDivider(
                color = colorResource(R.color.background_secondary)
            )

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Search results will appear here",
                    color = colorResource(R.color.subtitle)
                )
            }

        }
    }


}

@Composable
fun GpsScreenTopBar(
    modifier: Modifier = Modifier,
    locateUser: () -> Unit) {

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
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(Modifier.size(2.dp))

                Text(
                    text = "Aspen, USA",
                    fontSize = 12.sp
                )
            }

        }

        Spacer(Modifier.size(16.dp))

        OnBoardingButton(
            text = "Use Current Location",
            leadingIcon = Icons.Rounded.GpsFixed,
            onClick = locateUser
        )


    }

}

@Preview
@Composable
private fun GpsScreenPrev() {

    GpsScreen()

}