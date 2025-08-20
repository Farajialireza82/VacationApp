package com.cromulent.vacationapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.cromulent.vacationapp.common.shimmerEffect
import com.cromulent.vacationapp.model.LocationPhoto

@Composable
fun PhotoCard(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    contentScale: ContentScale = ContentScale.Crop,
    onPhotoClicked: (() -> Unit)? = null,
) {


    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    ).state

    if (imageState is AsyncImagePainter.State.Success) {

        Image(
            modifier = modifier
                .clickable(
                    enabled = onPhotoClicked != null,
                ){
                    onPhotoClicked?.invoke()
                },
            contentScale = contentScale,
            painter = imageState.painter,
            contentDescription = null
        )

    } else if (imageState is AsyncImagePainter.State.Loading) {

        Box(
            modifier = modifier
                .shimmerEffect()
                .fillMaxSize(),
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.LightGray)
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(32.dp),
                imageVector = Icons.Default.Photo,
                contentDescription = null,
                tint = Color.White
            )
        }
    }


}