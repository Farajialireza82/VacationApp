package com.cromulent.vacationapp.presentation.onBoardingScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.presentation.onBoardingScreen.components.OnBoardingButton
import com.cromulent.vacationapp.ui.theme.HiatusFont
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    viewmodel: OnBoardingViewModel
    ) {

    var page by remember { mutableIntStateOf(0) }
    var scope = rememberCoroutineScope()

    var pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )



    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        HorizontalPager(pagerState) {
            page = pagerState.currentPage

            OnBoardingPage(
                modifier = Modifier.fillMaxSize(),
                onBoardingData = pages[it]
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    vertical = 32.dp,
                    horizontal = 28.dp
                ),
        ) {

            Text(
                text = pages[page].title,
                color = Color.White,
                fontSize = 32.sp,
                lineHeight = 36.sp,
                modifier = Modifier
                    .width(180.dp)
                    .animateContentSize()
                    .padding(vertical = 12.dp)
            )

            OnBoardingButton(
                text = if (page == pages.size - 1) "Explore" else "Continue",
                onClick = {
                    if (page == pages.size - 1) {
                        viewmodel.saveAppEntry()
                        return@OnBoardingButton
                    } else {
                        scope.launch {
                            pagerState.nextPage()
                        }
                    }
                }
            )
        }
    }


}

fun PagerState.isOnLastPage(): Boolean {
    return currentPage == pageCount + 1
}

suspend fun PagerState.nextPage() {
    val nextPage = if (currentPage == pageCount + 1) 0 else currentPage + 1
    animateScrollToPage(nextPage)
}


@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    onBoardingData: OnBoardingData
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(onBoardingData.image),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0x00000000),
                            Color(0xA3000000)
                        )
                    )
                )
        )

        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 90.dp, horizontal = 12.dp),
            text = onBoardingData.locationName,
            fontSize = 90.sp,
            textAlign = TextAlign.Center,
            fontFamily = HiatusFont,
            letterSpacing = 6.sp,
            lineHeight = 120.sp,
            fontWeight = FontWeight.Bold,
            color = onBoardingData.titleColor
        )
    }
}

val pages = listOf(
    OnBoardingData(
        image = R.drawable.amesterdam,
        locationName = "Amsterdam",
        title = "Welcome to Travelo",
        titleColor = Color.Black
    ),
    OnBoardingData(
        image = R.drawable.oia_greece,
        locationName = "Santorini",
        title = "Plan your Luxurious Vacation"
    ),
    OnBoardingData(
        image = R.drawable.hotel_bavaro_dominican_republic,
        locationName = "Punta Cana",
        title = "Find the Best Hotels"
    ),
    OnBoardingData(
        image = R.drawable.moraine_lake_canada,
        locationName = "Alberta",
        title = "Live Life to the Fullest",
        titleColor = Color.Black
    ),
)