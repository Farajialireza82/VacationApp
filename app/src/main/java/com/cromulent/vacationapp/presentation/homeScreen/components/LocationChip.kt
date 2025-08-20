package com.cromulent.vacationapp.presentation.homeScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cromulent.vacationapp.R

@Composable
fun LocationChip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable{
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(16.dp),
            painter = painterResource(R.drawable.ic_location),
            contentDescription = "Location Icon"
        )

        Spacer(Modifier.size(6.dp))

        Text(
            text = text,
            color = Color.DarkGray,
            fontWeight = FontWeight.W300
        )

        Spacer(Modifier.size(6.dp))

        Icon(
            modifier = modifier.size(16.dp),
            imageVector = Icons.Default.KeyboardArrowDown,
            tint = colorResource(R.color.primary),
            contentDescription = ""
        )

    }
}