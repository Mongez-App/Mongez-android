package com.iti.mongez.presentation.auth.register.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.auth.usecase.RegisterUseCase
import com.iti.mongez.domain.auth.usecase.LoginWithGoogleUseCase
import com.iti.mongez.domain.core.exceptions.AppException
import com.iti.mongez.domain.preferences.usecase.CheckPreferencesSetUseCase
import com.iti.mongez.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.presentation.auth.register.uiState.RegisterEffect
import com.iti.mongez.presentation.auth.register.uiState.RegisterUiState
import com.iti.mongez.presentation.utils.toFriendlyMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val checkPreferencesSetUseCase: CheckPreferencesSetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.OnFirstNameChanged -> _state.update { it.copy(firstName = intent.name, firstNameError = null) }
            is RegisterIntent.OnEmailChanged -> _state.update { it.copy(email = intent.email, emailError = null) }
            is RegisterIntent.OnPasswordChanged -> _state.update { it.copy(password = intent.password, passwordError = null) }
            is RegisterIntent.OnConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = intent.password, confirmPasswordError = null) }
            is RegisterIntent.OnGoogleIdTokenReceived -> loginWithGoogle(intent.idToken)
            RegisterIntent.OnRegisterClicked -> register()
            // ViewModel tells the UI to launch the picker — the UI calls GoogleSignInManager
            // and returns the token via OnGoogleIdTokenReceived.
            RegisterIntent.OnGoogleSignUpClicked -> emitEffect(RegisterEffect.LaunchGoogleSignUp)
            RegisterIntent.OnLoginClicked -> emitEffect(RegisterEffect.NavigateToLogin)
            RegisterIntent.ClearFields -> _state.value = RegisterUiState()
        }
    }

    private fun register() {
        val currentState = state.value
        var hasError = false

        if (currentState.firstName.isBlank()) {
            _state.update { it.copy(firstNameError = "First name is required") }
            hasError = true
        }
        if (currentState.email.isBlank()) {
            _state.update { it.copy(emailError = "Email is required") }
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _state.update { it.copy(emailError = "Please enter a valid email address") }
            hasError = true
        }
        if (currentState.password.isBlank()) {
            _state.update { it.copy(passwordError = "Password is required") }
            hasError = true
        } else if (currentState.password.length < 8) {
            _state.update { it.copy(passwordError = "Password must be at least 8 characters") }
            hasError = true
        }
        if (currentState.password != currentState.confirmPassword) {
            _state.update { it.copy(confirmPasswordError = "Passwords do not match") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            registerUseCase(currentState.firstName, currentState.email, currentState.password).fold(
                onSuccess = { user ->
                    val isPreferencesSet = checkPreferencesSetUseCase()
                    _state.value = RegisterUiState()
                    emitEffect(RegisterEffect.NavigateToHome(user, isPreferencesSet))
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false) }
                    val message = (exception as? AppException)?.toFriendlyMessage()
                        ?: exception.message
                        ?: "Registration failed. Please try again."
                    emitEffect(RegisterEffect.ShowError(message))
                },
                onLoading = {
                    _state.update { it.copy(isLoading = true) }
                }
            )
        }
    }

    private fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            loginWithGoogleUseCase(idToken).fold(
                onSuccess = { user ->
                    val isPreferencesSet = checkPreferencesSetUseCase()
                    _state.value = RegisterUiState()
                    emitEffect(RegisterEffect.NavigateToHome(user, isPreferencesSet))
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false) }
                    val message = (exception as? AppException)?.toFriendlyMessage()
                        ?: exception.message
                        ?: "Google Sign-Up failed. Please try again."
                    emitEffect(RegisterEffect.ShowError(message))
                },
                onLoading = {
                    _state.update { it.copy(isLoading = true) }
                }
            )
        }
    }

    private fun emitEffect(effect: RegisterEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
