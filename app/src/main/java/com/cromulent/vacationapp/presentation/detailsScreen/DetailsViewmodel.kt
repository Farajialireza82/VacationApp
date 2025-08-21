package com.cromulent.vacationapp.presentation.detailsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.util.Resource
import com.cromulent.vacationapp.util.Samples
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewmodel @Inject constructor(
    val vacationRepository: VacationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var locationId: String? = savedStateHandle["location_id"]

    private val _state = MutableStateFlow<DetailsState>(DetailsState())
    val state: StateFlow<DetailsState> = _state.asStateFlow()


    init {
        getLocationDetails()
    }

    fun getLocationDetails(forceRefresh: Boolean = false) {

        locationId ?: return

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )


        viewModelScope.launch {

            vacationRepository
                .getLocationDetails(locationId!!, forceRefresh)
                .collectLatest { resource ->

                    when(resource){
                        is Resource.Error<*> -> {
                            _state.value = _state.value.copy(
                                error = resource.message,
                                isLoading = false,
                                location = null)
                        }
                        is Resource.Success<*> -> {
                            _state.value = _state.value.copy(location = resource.data, isLoading = false)

                        }
                    }

                }
        }

    }
}