package com.cromulent.vacationapp.presentation.onBoardingScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cromulent.vacationapp.domain.manager.LocalUserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private var localUserManager: LocalUserManager
) : ViewModel() {

    fun saveAppEntry(){
        viewModelScope.launch {
            localUserManager.saveAppEntry()
        }
    }

}