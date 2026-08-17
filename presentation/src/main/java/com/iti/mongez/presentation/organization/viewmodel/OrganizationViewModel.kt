package com.iti.mongez.presentation.organization.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.organization.usecase.GetDiscoverTeamsUseCase
import com.iti.mongez.domain.organization.usecase.GetMyTeamsUseCase
import com.iti.mongez.domain.organization.usecase.JoinTeamUseCase
import com.iti.mongez.presentation.organization.contract.OrganizationEffect
import com.iti.mongez.presentation.organization.contract.OrganizationIntent
import com.iti.mongez.presentation.organization.uiState.JoinTeamState
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@HiltViewModel
class OrganizationViewModel @Inject constructor(
    private val getMyTeamsUseCase: GetMyTeamsUseCase,
    private val getDiscoverTeamsUseCase: GetDiscoverTeamsUseCase,
    private val joinTeamUseCase: JoinTeamUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<OrganizationUiState>(OrganizationUiState.Loading)
    val state: StateFlow<OrganizationUiState> = _state.asStateFlow()

    private val _joinTeamState = MutableStateFlow<JoinTeamState>(JoinTeamState.Idle)
    val joinTeamState: StateFlow<JoinTeamState> = _joinTeamState.asStateFlow()

    private val _effect = MutableSharedFlow<OrganizationEffect>()
    val effect: SharedFlow<OrganizationEffect> = _effect.asSharedFlow()

    private var pollingJob: Job? = null

    init {
        handleIntent(OrganizationIntent.LoadData)
    }

    fun handleIntent(intent: OrganizationIntent) {
        when (intent) {
            is OrganizationIntent.LoadData -> loadData()
            is OrganizationIntent.Refresh -> loadData(intent.showLoading)
            is OrganizationIntent.JoinTeam -> joinTeam(intent.inviteCode)
            is OrganizationIntent.ResetJoinTeamState -> _joinTeamState.value = JoinTeamState.Idle
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

    private fun joinTeam(inviteCode: String) {
        viewModelScope.launch {
            _joinTeamState.value = JoinTeamState.Loading
            val result = joinTeamUseCase(inviteCode)
            result.fold(
                onSuccess = { response ->
                    if (response.success) {
                        _joinTeamState.value = JoinTeamState.Success(response.message ?: "Successfully joined the team")
                        // Reload data to update pending/trending lists
                        loadData(showLoading = false)
                    } else {
                        _joinTeamState.value = JoinTeamState.Error(response.error ?: response.message ?: "Failed to join team")
                    }
                },
                onFailure = { error ->
                    _joinTeamState.value = JoinTeamState.Error(error.message ?: "An unexpected error occurred")
                },
                onLoading = {}
            )
        }
    }

    fun startPolling(intervalMs: Long = 5000L) {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            while (isActive) {
                delay(intervalMs)
                if (_state.value !is OrganizationUiState.Loading) {
                    loadData(showLoading = false)
                }
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }
}
