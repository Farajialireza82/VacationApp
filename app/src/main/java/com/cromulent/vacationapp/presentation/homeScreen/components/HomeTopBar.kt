package com.cromulent.vacationapp.presentation.homeScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.ui.theme.NeonBlitz

@Composable
fun HomeTopBar(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .height(64.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Text(
                text = "Explore",
                fontSize = 14.sp
            )
            Text(
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.Bold,
                fontFamily = NeonBlitz,
                color = colorResource(R.color.primary),
                fontSize = 28.sp
            )
        }

       LocationChip()
    }
}

@Preview
@Composable
private fun HomeTBPrev() {

    HomeTopBar()
    
}