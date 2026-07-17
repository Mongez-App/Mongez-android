package com.iti.mongez.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.auth.usecase.RegisterUseCase
import com.iti.mongez.domain.core.Result
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
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.OnFirstNameChanged -> _state.update { it.copy(firstName = intent.name, firstNameError = null) }
            is RegisterIntent.OnEmailChanged -> _state.update { it.copy(email = intent.email, emailError = null) }
            is RegisterIntent.OnPasswordChanged -> _state.update { it.copy(password = intent.password, passwordError = null) }
            is RegisterIntent.OnConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = intent.password, confirmPasswordError = null) }
            RegisterIntent.OnRegisterClicked -> register()
            RegisterIntent.OnGoogleSignUpClicked -> handleGoogleSignUp()
            RegisterIntent.OnLoginClicked -> emitEffect(RegisterEffect.NavigateToLogin)
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
        }
        if (currentState.password.isBlank()) {
            _state.update { it.copy(passwordError = "Password is required") }
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
                    _state.update { it.copy(isLoading = false) }
                    emitEffect(RegisterEffect.NavigateToHome(user))
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false) }
                    emitEffect(RegisterEffect.ShowError(exception.message ?: "Registration failed"))
                },
                onLoading = {
                    _state.update { it.copy(isLoading = true) }
                }
            )
        }
    }

    private fun handleGoogleSignUp() {
        emitEffect(RegisterEffect.ShowError("Google Sign-Up not implemented yet"))
    }

    private fun emitEffect(effect: RegisterEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
