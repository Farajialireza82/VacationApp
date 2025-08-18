package com.cromulent.vacationapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
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
import androidx.compose.ui.text.font.FontWeight
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
import com.cromulent.vacationapp.util.Samples.location

@Composable
fun CompactLocationCard(
    modifier: Modifier = Modifier,
    location: Location?,
    onClick: (Location) -> Unit
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
            .width(174.dp)
            .height(142.dp)
            .background(
                color = colorResource(R.color.background_tertiary),
                shape = RoundedCornerShape(16.dp)
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .height(96.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {

                if (imageState is AsyncImagePainter.State.Success) {

                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = imageState.painter,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                } else if (imageState is AsyncImagePainter.State.Loading) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerEffect()
                    ) {}
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
            }


            Text(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxWidth(),
                text = location?.name ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
            )

        }


    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CompactLocationCardPrev() {

    VacationAppTheme{
        CompactLocationCard(
            location = location
        ){}
    }
    
}