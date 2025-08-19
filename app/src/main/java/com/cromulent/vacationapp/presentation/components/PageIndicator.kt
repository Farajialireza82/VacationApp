package com.cromulent.vacationapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageSize: Int,
    currentPage: Int,
    maxVisibleDots: Int = 7
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // If too many pages, only show up to maxVisibleDots with fading edges
        val half = maxVisibleDots / 2
        val start = when {
            currentPage <= half -> 0
            currentPage >= pageSize - half -> pageSize - maxVisibleDots
            else -> currentPage - half
        }.coerceAtLeast(0)

        val end = (start + maxVisibleDots).coerceAtMost(pageSize)

        for (i in start until end) {
            val isSelected = i == currentPage
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(if (isSelected) 10.dp else 6.dp) // selected dot is bigger
                    .clip(CircleShape)
                    .background(if (isSelected) Color.DarkGray else Color.LightGray)
            )
        }

        // Show overflow indicator if there are more pages
        if (end < pageSize) {
            Text("…", modifier = Modifier.padding(start = 4.dp), color = Color.Gray)
        }
    }
}