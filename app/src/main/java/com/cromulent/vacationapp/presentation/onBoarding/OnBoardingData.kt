package com.cromulent.vacationapp.presentation.onBoarding

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class OnBoardingData(
    val locationName: String,
    val title: String,
    @DrawableRes val image: Int,
    val titleColor : Color = Color.White
)
