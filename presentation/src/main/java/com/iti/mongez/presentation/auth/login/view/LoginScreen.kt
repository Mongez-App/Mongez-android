package com.iti.mongez.presentation.auth.login.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.SocialButton
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.components.textfield.AppPasswordTextField
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.theme.Theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.iti.mongez.designsystem.R as DesignSystemR
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.auth.rememberGoogleSignInLauncher
import com.iti.mongez.presentation.auth.login.contract.LoginIntent
import com.iti.mongez.presentation.auth.login.uiState.LoginEffect
import com.iti.mongez.presentation.auth.login.uiState.LoginUiState
import com.iti.mongez.presentation.auth.login.viewmodel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
    onNavigateToHome: (Boolean) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var topErrorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    
    val launchGoogleSignIn = rememberGoogleSignInLauncher(
        onResult = { result ->
            result.onSuccess { token ->
                viewModel.onIntent(LoginIntent.OnGoogleIdTokenReceived(token))
            }.onFailure { e ->
                topErrorMessage = e.message ?: "Google Sign-In failed"
                // The LaunchedEffect inside the UI manages delay elsewhere if we want, 
                // but for now we can rely on a LaunchedEffect tied to the error message.
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
                is LoginEffect.NavigateToHome -> onNavigateToHome(effect.isPreferencesSet)
                LoginEffect.NavigateToSignUp -> onNavigateToSignUp()
                LoginEffect.NavigateToForgotPassword -> onShowSnackbar("Forgot password clicked")
                is LoginEffect.ShowError -> {
                    topErrorMessage = effect.message
                }
                LoginEffect.LaunchGoogleSignIn -> {
                    launchGoogleSignIn()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginContent(
            state = state,
            onIntent = viewModel::onIntent
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
private fun LoginContent(
    state: LoginUiState,
    onIntent: (LoginIntent) -> Unit
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

        // Header
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = Theme.spacing.md, top = 4.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = DesignSystemR.drawable.ic_sparkle),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(Theme.spacing.xs))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Theme.colorScheme.border.secondary.copy(alpha = 0.5f))
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.welcome_back),
                    style = Theme.typography.headline.medium.copy(fontWeight = FontWeight.Bold),
                    color = Theme.colorScheme.text.primary
                )
                Spacer(modifier = Modifier.height(Theme.spacing.xs))
                Text(
                    text = stringResource(R.string.login_subtitle),
                    style = Theme.typography.body.large,
                    color = Theme.colorScheme.text.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xxl))

        // Form
        AppTextField(
            value = state.email,
            onValueChange = { onIntent(LoginIntent.OnEmailChanged(it)) },
            label = stringResource(R.string.email_label),
            placeholder = stringResource(R.string.email_placeholder),
            leadingIcon = Icons.Outlined.Email,
            isError = state.emailError != null,
            errorMessage = state.emailError
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        AppPasswordTextField(
            value = state.password,
            onValueChange = { onIntent(LoginIntent.OnPasswordChanged(it)) },
            label = stringResource(R.string.password_label),
            placeholder = stringResource(R.string.password_placeholder),
            leadingIcon = Icons.Outlined.Lock,
            imeAction = ImeAction.Done,
            isError = state.passwordError != null,
            errorMessage = state.passwordError
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        // Forgot Password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.forgot_password),
                style = Theme.typography.label.large,
                color = Theme.colorScheme.brand.primary,
                modifier = Modifier.clickable { onIntent(LoginIntent.OnForgotPasswordClicked) }
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        // Continue Button
        AppButton(
            text = stringResource(R.string.continue_btn),
            onClick = { onIntent(LoginIntent.OnLoginClicked) },
            isLoading = state.isLoading,
            fullWidth = true
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        // OR Divider
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

        // Social Buttons

        SocialButton(
            text = stringResource(R.string.sign_in_google),
            icon = ImageVector.vectorResource(id = DesignSystemR.drawable.ic_google),
            onClick = { onIntent(LoginIntent.OnGoogleSignInClicked) }
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        // Sign Up Link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dont_have_account),
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.text.secondary
            )
            Text(
                text = stringResource(R.string.sign_up),
                style = Theme.typography.label.large,
                color = Theme.colorScheme.brand.primary,
                modifier = Modifier.clickable { onIntent(LoginIntent.OnSignUpClicked) }
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xl))
    }
}
