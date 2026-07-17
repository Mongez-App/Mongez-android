package com.iti.mongez.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.auth.usecase.LoginUseCase
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
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

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
            LoginIntent.OnLoginClicked -> login()
            LoginIntent.OnGuestClicked -> navigateToHome(null)
            LoginIntent.OnGoogleSignInClicked -> handleGoogleSignIn()
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
                    emitEffect(LoginEffect.ShowError(exception.message ?: "Login failed"))
                },
                onLoading = {
                    _state.update { it.copy(isLoading = true) }
                }
            )
        }
    }

    private fun handleGoogleSignIn() {
        // Not implemented in this phase
        emitEffect(LoginEffect.ShowError("Google Sign-In not implemented yet"))
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
