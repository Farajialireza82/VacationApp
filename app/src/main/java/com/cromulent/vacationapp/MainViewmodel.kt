package com.cromulent.vacationapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cromulent.vacationapp.domain.manager.LocalUserManager
import com.cromulent.vacationapp.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    localUserManager: LocalUserManager
) : ViewModel() {

    var startDestination by mutableStateOf("")
        private set


    init {
        localUserManager.readAppEntry().onEach { startFromHomeScreen ->
            delay(600)
            startDestination =
                if (startFromHomeScreen) Route.HomeNavigation.route else Route.AppStartNavigation.route
        }.launchIn(viewModelScope)
    }
}