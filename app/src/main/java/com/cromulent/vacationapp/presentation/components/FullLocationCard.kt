package com.cromulent.vacationapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.common.shimmerEffect
import com.cromulent.vacationapp.model.Location
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import com.cromulent.vacationapp.util.Samples

@Composable
fun FullLocationCard(
    modifier: Modifier = Modifier,
    location: Location?,
    isBookmarked: Boolean = false,
    onClick: (Location?) -> Unit
) {

    val locationPhotos = location?.locationPhotos

    val imageUrl =
        if (locationPhotos?.isNotEmpty() == true) locationPhotos[0].images.large.url else ""

    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Box(
        modifier = modifier
            .width(188.dp)
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                onClick = { onClick(location) }
            )
    ) {

        if (imageState is AsyncImagePainter.State.Success) {

            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                painter = imageState.painter,
                contentDescription = null,
            )
        } else if (imageState is AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )
        } else {
            Box(
                Modifier
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

        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            location?.name?.let {
                NameCard(name = it)
            }

            if (isBookmarked) {

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_heart),
                        contentDescription = null
                    )

                }
            }
        }

    }

}

@Composable
fun NameCard(
    modifier: Modifier = Modifier,
    name: String
) {
    Box(
        modifier = modifier
            .width(100.dp)
            .height(24.dp)
            .background(
                color = Color(0xFF4e5753),
                shape = RoundedCornerShape(59.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = name,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun FullLocationCardPrev() {
    VacationAppTheme {
        FullLocationCard(
            location = Samples.location
        ) {}
    }
}