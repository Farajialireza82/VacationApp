package com.cromulent.vacationapp.presentation.navigation

sealed class Route(
    val route: String
) {

    object OnBoardingScreen: Route(route = "onBoardingScreen")
    object AppStartNavigation: Route(route = "appStartNavigation")
    object HomeScreen: Route(route = "homeScreen")
    object HomeNavigation: Route(route = "homeNavigation")



}