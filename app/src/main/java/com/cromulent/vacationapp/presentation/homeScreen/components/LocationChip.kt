package com.cromulent.vacationapp.presentation.homeScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cromulent.vacationapp.R

@Composable
fun LocationChip(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                onClick()
            }
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(16.dp)
                .wrapContentWidth(),
            painter = painterResource(R.drawable.ic_location),
            contentDescription = "Location Icon"
        )

        Text(
            modifier = Modifier
                .padding(start = 2.dp, end = 6.dp)
                .weight(1f),
            text = text,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            color = Color.DarkGray,
            fontWeight = FontWeight.W300
        )

        Image(
            modifier = Modifier
                .size(16.dp),
            imageVector = Icons.Default.KeyboardArrowDown,
            colorFilter = ColorFilter.tint(colorResource(R.color.primary)),
            contentDescription = ""
        )

    }
}

@Preview
@Composable
private fun Preview() {

    LocationChip(text = "Paris, THE GREAT FRANCE") { }

}