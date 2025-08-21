package com.cromulent.vacationapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cromulent.vacationapp.presentation.detailsScreen.DetailsScreen
import com.cromulent.vacationapp.presentation.detailsScreen.DetailsViewmodel
import com.cromulent.vacationapp.presentation.gpsScreen.GpsScreen
import com.cromulent.vacationapp.presentation.gpsScreen.GpsViewmodel
import com.cromulent.vacationapp.presentation.homeScreen.HomeScreen
import com.cromulent.vacationapp.presentation.homeScreen.HomeViewmodel
import com.cromulent.vacationapp.presentation.onBoardingScreen.OnBoardingScreen
import com.cromulent.vacationapp.presentation.onBoardingScreen.OnBoardingViewModel
import com.cromulent.vacationapp.presentation.searchScreen.SearchScreen
import com.cromulent.vacationapp.presentation.searchScreen.SearchViewmodel
import kotlin.collections.listOf

@Composable
fun MainNavGraph(
    startDestination: String,
    modifier: Modifier = Modifier
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
                val viewmodel = hiltViewModel<HomeViewmodel>()
                HomeScreen(
                    modifier = modifier,
                    viewmodel = viewmodel,
                    openDetailsScreen = {
                        navController.navigate(Route.DetailsScreen.route + "/" + it)
                    },
                    openLocationPickerScreen = {
                        navController.navigate(Route.GpsScreen.route)
                    },
                    openSearchScreen = {
                        navController.navigate(Route.SearchScreen.route)
                    }
                )
            }

            composable(
                route = Route.DetailsScreen.route + "/{location_id}",
                arguments = listOf(
                    navArgument("location_id") { type = NavType.StringType }
                )
            ) {
                val viewmodel = hiltViewModel<DetailsViewmodel>()

                DetailsScreen(viewmodel = viewmodel)

            }

            composable(
                route = Route.GpsScreen.route,
            ) {
                val viewmodel = hiltViewModel<GpsViewmodel>()
                GpsScreen(
                    viewmodel = viewmodel
                )
            }

            composable(
                route = Route.SearchScreen.route,
            ) {
                val viewmodel = hiltViewModel<SearchViewmodel>()
                SearchScreen(
                    viewmodel = viewmodel
                ){ locationId ->
                    navController.navigate(Route.DetailsScreen.route + "/" + locationId)
                }
            }
        }
    }
}