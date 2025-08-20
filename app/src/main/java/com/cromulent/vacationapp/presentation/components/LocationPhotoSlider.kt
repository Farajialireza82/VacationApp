package com.cromulent.vacationapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.model.LocationPhoto

@Composable
fun LocationPhotoSlider(
    modifier: Modifier = Modifier,
    photos: List<LocationPhoto>?,
    initialPage: Int = 0,
    contentScale: ContentScale = ContentScale.Crop,
    onPhotoClicked: ((LocationPhoto) -> Unit)? = null,
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        if (photos.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.LightGray)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp),
                    imageVector = Icons.Default.NoPhotography,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            return
        }

        val state = rememberPagerState(initialPage = initialPage) { photos.size }

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            state = state
        ) {

            PhotoCard(
                modifier = Modifier.fillMaxSize(),
                imageUrl = photos[it].images.large.url,
                contentScale = contentScale,
                onPhotoClicked = onPhotoClicked?.let { callback ->
                    { callback(photos[it]) }
                }
            )

        }

        PageIndicator(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter),
            pageSize = photos.size,
            currentPage = state.currentPage
        )

    }

}