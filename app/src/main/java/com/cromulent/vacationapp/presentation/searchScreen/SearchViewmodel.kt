package com.cromulent.vacationapp.presentation.searchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewmodel @Inject constructor(
    val vacationRepository: VacationRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    fun search(category: String, query: String) {

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {

            vacationRepository
                .searchLocation(category, query)
                .collectLatest { resource ->

                    when (resource) {
                        is Resource.Error<*> -> {
                            _state.value = _state.value.copy(
                                error = resource.message,
                                isLoading = false
                            )
                        }

                        is Resource.Success<*> -> {
                            _state.value = _state.value.copy(
                                data = resource.data,
                                error = null,
                                isLoading = false
                            )

                        }
                    }

                }
        }

    }
}