package com.iti.mongez.presentation.auth.register.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.SocialButton
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.components.textfield.AppPasswordTextField
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.theme.Theme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.R as DesignSystemR
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.auth.rememberGoogleSignInLauncher
import com.iti.mongez.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.presentation.auth.register.uiState.RegisterEffect
import com.iti.mongez.presentation.auth.register.uiState.RegisterUiState
import com.iti.mongez.presentation.auth.register.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel<RegisterViewModel>(),
    onNavigateToHome: (Boolean) -> Unit,
    onNavigateToLogin: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var topErrorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    
    val launchGoogleSignIn = rememberGoogleSignInLauncher(
        onResult = { result ->
            result.onSuccess { token ->
                viewModel.onIntent(RegisterIntent.OnGoogleIdTokenReceived(token))
            }.onFailure { e ->
                topErrorMessage = e.message ?: "Google Sign-Up failed"
            }
        }
    )

    LaunchedEffect(topErrorMessage) {
        if (topErrorMessage != null) {
            delay(3000)
            topErrorMessage = null
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is RegisterEffect.NavigateToHome -> onNavigateToHome(effect.isPreferencesSet)
                RegisterEffect.NavigateToLogin -> onNavigateToLogin()
                is RegisterEffect.ShowError -> {
                    topErrorMessage = effect.message
                }
                RegisterEffect.LaunchGoogleSignUp -> {
                    launchGoogleSignIn()
                }
            }
        }
    }

    RegisterScreenContent(
        state = state,
        topErrorMessage = topErrorMessage,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun RegisterScreenContent(
    state: RegisterUiState,
    topErrorMessage: String?,
    onIntent: (RegisterIntent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        RegisterContent(
            state = state,
            onIntent = onIntent
        )

        AnimatedVisibility(
            visible = topErrorMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(Theme.spacing.md)
                .padding(top = 32.dp)
        ) {
            topErrorMessage?.let { message ->
                AppSnackbarContent(
                    message = message,
                    type = AppSnackbarType.Error
                )
            }
        }
    }
}

@Composable
private fun RegisterContent(
    state: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.surface.background)
            .padding(Theme.spacing.xl)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Theme.spacing.xxl))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.create_account),
                    style = Theme.typography.display.small,
                    color = Theme.colorScheme.text.primary
                )
                Spacer(modifier = Modifier.height(Theme.spacing.xs))
                Text(
                    text = stringResource(R.string.register_subtitle),
                    style = Theme.typography.body.large,
                    color = Theme.colorScheme.text.secondary
                )
            }


        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        AppTextField(
            value = state.firstName,
            onValueChange = { onIntent(RegisterIntent.OnFirstNameChanged(it)) },
            label = stringResource(R.string.first_name_label),
            placeholder = stringResource(R.string.first_name_placeholder),
            leadingIcon = Icons.Outlined.Person,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            isError = state.firstNameError != null,
            errorMessage = state.firstNameError
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppTextField(
            value = state.email,
            onValueChange = { onIntent(RegisterIntent.OnEmailChanged(it)) },
            label = stringResource(R.string.email_label),
            leadingIcon = Icons.Outlined.Email,
            placeholder = stringResource(R.string.email_placeholder),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = state.emailError != null,
            errorMessage = state.emailError
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppPasswordTextField(
            value = state.password,
            onValueChange = { onIntent(RegisterIntent.OnPasswordChanged(it)) },
            label = stringResource(R.string.password_label),
            placeholder = stringResource(R.string.password_placeholder),
            leadingIcon = Icons.Outlined.Lock,
            imeAction = ImeAction.Next,
            isError = state.passwordError != null,
            errorMessage = state.passwordError
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppPasswordTextField(
            value = state.confirmPassword,
            onValueChange = { onIntent(RegisterIntent.OnConfirmPasswordChanged(it)) },
            label = stringResource(R.string.confirm_password_label),
            placeholder = stringResource(R.string.password_placeholder),
            leadingIcon = Icons.Outlined.Lock,
            imeAction = ImeAction.Done,
            isError = state.confirmPasswordError != null,
            errorMessage = state.confirmPasswordError
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        AppButton(
            text = stringResource(R.string.create_account_btn),
            onClick = { onIntent(RegisterIntent.OnRegisterClicked) },
            isLoading = state.isLoading,
            fullWidth = true
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Theme.colorScheme.border.primary.copy(alpha = 0.5f)
            )
            Text(
                text = stringResource(R.string.or_divider),
                modifier = Modifier.padding(horizontal = Theme.spacing.md),
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.text.tertiary
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Theme.colorScheme.border.primary.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        SocialButton(
            text = stringResource(R.string.sign_up_google),
            icon = ImageVector.vectorResource(id = DesignSystemR.drawable.ic_google),
            onClick = { onIntent(RegisterIntent.OnGoogleSignUpClicked) }
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                style = Theme.typography.body.large,
                color = Theme.colorScheme.text.secondary
            )
            Spacer(modifier = Modifier.width(Theme.spacing.xs))
            Text(
                text = stringResource(R.string.log_in),
                style = Theme.typography.body.large,
                color = Theme.colorScheme.brand.primary,
                modifier = Modifier.clickable { onIntent(RegisterIntent.OnLoginClicked) }
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xl))
    }
}

@Preview(showBackground = true, name = "Register Screen - Light Mode")
@Composable
private fun RegisterScreenPreview() {
    MongezTheme {
        RegisterScreenContent(
            state = RegisterUiState(
                firstName = "John Doe",
                email = "john@example.com"
            ),
            topErrorMessage = null,
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Register Screen - Loading")
@Composable
private fun RegisterScreenLoadingPreview() {
    MongezTheme {
        RegisterScreenContent(
            state = RegisterUiState(
                firstName = "John Doe",
                email = "john@example.com",
                isLoading = true
            ),
            topErrorMessage = null,
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Register Screen - Validation Errors")
@Composable
private fun RegisterScreenErrorPreview() {
    MongezTheme {
        RegisterScreenContent(
            state = RegisterUiState(
                firstName = "",
                firstNameError = "First name is required",
                email = "invalid-email",
                emailError = "Invalid email format",
                password = "123",
                passwordError = "Password too short",
                confirmPassword = "456",
                confirmPasswordError = "Passwords do not match"
            ),
            topErrorMessage = null,
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Register Screen - Top Error")
@Composable
private fun RegisterScreenTopErrorPreview() {
    MongezTheme {
        RegisterScreenContent(
            state = RegisterUiState(
                firstName = "John Doe",
                email = "john@example.com"
            ),
            topErrorMessage = "Account already exists",
            onIntent = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Register Screen - Dark Mode"
)
@Composable
private fun RegisterScreenDarkPreview() {
    MongezTheme(darkTheme = true) {
        RegisterScreenContent(
            state = RegisterUiState(
                firstName = "John Doe",
                email = "john@example.com"
            ),
            topErrorMessage = null,
            onIntent = {}
        )
    }
}
