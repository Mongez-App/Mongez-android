package com.iti.mongez.presentation.organization.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.organization.usecase.GetDiscoverTeamsUseCase
import com.iti.mongez.domain.organization.usecase.GetMyTeamsUseCase
import com.iti.mongez.presentation.organization.contract.OrganizationEffect
import com.iti.mongez.presentation.organization.contract.OrganizationIntent
import com.iti.mongez.presentation.organization.uiState.OrganizationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.async

@HiltViewModel
class OrganizationViewModel @Inject constructor(
    private val getMyTeamsUseCase: GetMyTeamsUseCase,
    private val getDiscoverTeamsUseCase: GetDiscoverTeamsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<OrganizationUiState>(OrganizationUiState.Loading)
    val state: StateFlow<OrganizationUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<OrganizationEffect>()
    val effect: SharedFlow<OrganizationEffect> = _effect.asSharedFlow()

    init {
        handleIntent(OrganizationIntent.LoadData)
    }

    fun handleIntent(intent: OrganizationIntent) {
        when (intent) {
            is OrganizationIntent.LoadData -> loadData()
            is OrganizationIntent.Refresh -> loadData(intent.showLoading)
        }
    }

    private fun loadData(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _state.value = OrganizationUiState.Loading
            }

            val myTeamsDeferred = async { getMyTeamsUseCase() }
            val discoverTeamsDeferred = async { getDiscoverTeamsUseCase() }

            val myTeamsResult = myTeamsDeferred.await()
            val discoverTeamsResult = discoverTeamsDeferred.await()

            myTeamsResult.fold(
                onSuccess = { myTeams ->
                    discoverTeamsResult.fold(
                        onSuccess = { discoverTeams ->
                            _state.value = OrganizationUiState.Success(
                                myTeams = myTeams,
                                discoverTeams = discoverTeams
                            )
                        },
                        onFailure = { error ->
                            _state.value = OrganizationUiState.Error(error.message ?: "Failed to load discover teams")
                        },
                        onLoading = {}
                    )
                },
                onFailure = { error ->
                    _state.value = OrganizationUiState.Error(error.message ?: "Failed to load my teams")
                },
                onLoading = {}
            )
        }
    }
}
