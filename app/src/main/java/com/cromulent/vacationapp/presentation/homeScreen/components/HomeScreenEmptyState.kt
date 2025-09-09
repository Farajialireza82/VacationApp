package com.cromulent.vacationapp.presentation.homeScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.ui.theme.NeonBlitz

@Composable
fun HomeScreenEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier
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
}