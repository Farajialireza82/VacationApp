package com.cromulent.vacationapp.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.cromulent.vacationapp.presentation.home.HomeScreen
import com.cromulent.vacationapp.presentation.home.HomeViewmodel
import com.cromulent.vacationapp.presentation.onBoarding.OnBoardingScreen
import com.cromulent.vacationapp.presentation.onBoarding.OnBoardingViewModel

@Composable
fun MainNavGraph(
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    if (startDestination.isEmpty()) return

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(
                route = Route.OnBoardingScreen.route
            ) {
                val viewmodel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(
                    modifier = modifier,
                    viewmodel = viewmodel
                )
            }
        }

        navigation(
            route = Route.HomeNavigation.route,
            startDestination = Route.HomeScreen.route
        ) {
            composable(
                route = Route.HomeScreen.route
            ) {
                val viewmodel: HomeViewmodel = hiltViewModel()
                HomeScreen(
                    modifier = modifier,
                    viewmodel = viewmodel
                )
            }
        }
    }
}