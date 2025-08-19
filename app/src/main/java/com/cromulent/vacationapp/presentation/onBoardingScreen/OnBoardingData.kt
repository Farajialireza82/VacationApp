package com.cromulent.vacationapp.presentation.onBoardingScreen

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class OnBoardingData(
    val locationName: String,
    val title: String,
    @DrawableRes val image: Int,
    val titleColor : Color = Color.White
)
