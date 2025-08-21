package com.cromulent.vacationapp.presentation.navigation

sealed class Route(
    val route: String
) {

    object AppStartNavigation: Route(route = "appStartNavigation")
    object HomeNavigation: Route(route = "homeNavigation")

    object OnBoardingScreen: Route(route = "onBoardingScreen")
    object HomeScreen: Route(route = "homeScreen")
    object DetailsScreen: Route(route = "detailsScreen")
    object GpsScreen: Route(route = "GpsScreen")



}