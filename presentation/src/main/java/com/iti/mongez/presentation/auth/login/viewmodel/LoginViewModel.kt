package com.iti.mongez.presentation.auth.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.auth.usecase.LoginUseCase
import com.iti.mongez.domain.auth.usecase.LoginWithGoogleUseCase
import com.iti.mongez.domain.core.exceptions.AppException
import com.iti.mongez.presentation.auth.login.contract.LoginIntent
import com.iti.mongez.presentation.auth.login.uiState.LoginEffect
import com.iti.mongez.presentation.auth.login.uiState.LoginUiState
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnEmailChanged -> {
                _state.update { it.copy(email = intent.email, emailError = null) }
            }
            is LoginIntent.OnPasswordChanged -> {
                _state.update { it.copy(password = intent.password, passwordError = null) }
            }
            is LoginIntent.OnGoogleIdTokenReceived -> loginWithGoogle(intent.idToken)
            LoginIntent.OnLoginClicked -> login()
            LoginIntent.OnGuestClicked -> navigateToHome(null)
            // ViewModel tells the UI to launch the picker — the UI calls GoogleSignInManager
            // and returns the token via OnGoogleIdTokenReceived. This keeps Activity context
            // (required by CredentialManager) in the UI layer where it belongs.
            LoginIntent.OnGoogleSignInClicked -> emitEffect(LoginEffect.LaunchGoogleSignIn)
            LoginIntent.OnSignUpClicked -> emitEffect(LoginEffect.NavigateToSignUp)
            LoginIntent.OnForgotPasswordClicked -> emitEffect(LoginEffect.NavigateToForgotPassword)
        }
    }

    private fun login() {
        val currentState = state.value
        if (currentState.email.isBlank()) {
            _state.update { it.copy(emailError = "Email cannot be empty") }
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _state.update { it.copy(emailError = "Please enter a valid email address") }
            return
        }
        if (currentState.password.isBlank()) {
            _state.update { it.copy(passwordError = "Password cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            loginUseCase(currentState.email, currentState.password).fold(
                onSuccess = { user ->
                    _state.update { it.copy(isLoading = false) }
                    emitEffect(LoginEffect.NavigateToHome(user))
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false) }
                    val message = (exception as? AppException)?.toFriendlyMessage()
                        ?: exception.message
                        ?: "Login failed. Please try again."
                    emitEffect(LoginEffect.ShowError(message))
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
                    _state.update { it.copy(isLoading = false) }
                    emitEffect(LoginEffect.NavigateToHome(user))
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false) }
                    val message = (exception as? AppException)?.toFriendlyMessage()
                        ?: exception.message
                        ?: "Google Sign-In failed. Please try again."
                    emitEffect(LoginEffect.ShowError(message))
                },
                onLoading = {
                    _state.update { it.copy(isLoading = true) }
                }
            )
        }
    }

    private fun navigateToHome(user: User?) {
        emitEffect(LoginEffect.NavigateToHome(user))
    }

    private fun emitEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
