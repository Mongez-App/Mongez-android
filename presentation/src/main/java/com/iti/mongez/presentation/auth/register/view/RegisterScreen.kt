package com.iti.mongez.presentation.auth.register.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.SocialButton
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.components.textfield.AppPasswordTextField
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.theme.Theme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.iti.mongez.designsystem.R as DesignSystemR
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.presentation.auth.register.uiState.RegisterEffect
import com.iti.mongez.presentation.auth.register.uiState.RegisterUiState
import com.iti.mongez.presentation.auth.register.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel<RegisterViewModel>(),
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var topErrorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is RegisterEffect.NavigateToHome -> onNavigateToHome()
                RegisterEffect.NavigateToLogin -> onNavigateToLogin()
                is RegisterEffect.ShowError -> {
                    topErrorMessage = effect.message
                    delay(3000)
                    topErrorMessage = null
                }
                RegisterEffect.LaunchGoogleSignUp -> {
                    coroutineScope.launch {
                        try {
                            val credentialManager = CredentialManager.create(context)
                            val googleIdOption = GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId(context.getString(R.string.default_web_client_id))
                                .setAutoSelectEnabled(true)
                                .build()
                            val request = GetCredentialRequest.Builder()
                                .addCredentialOption(googleIdOption)
                                .build()
                            val result = credentialManager.getCredential(context, request)
                            val credential = result.credential
                            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                viewModel.onIntent(RegisterIntent.OnGoogleIdTokenReceived(googleIdTokenCredential.idToken))
                            } else {
                                onShowSnackbar("Unknown credential type")
                            }
                        } catch (e: Exception) {
                            onShowSnackbar(e.message ?: "Google Sign-Up failed")
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        RegisterContent(
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
                    text = stringResource(R.string.create_account),
                    style = Theme.typography.headline.medium.copy(fontWeight = FontWeight.Bold),
                    color = Theme.colorScheme.text.primary
                )
                Spacer(modifier = Modifier.height(Theme.spacing.xs))
                Text(
                    text = stringResource(R.string.register_subtitle),
                    style = Theme.typography.body.large,
                    color = Theme.colorScheme.text.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        // Form Fields
        AppTextField(
            value = state.firstName,
            onValueChange = { onIntent(RegisterIntent.OnFirstNameChanged(it)) },
            label = stringResource(R.string.first_name_label),
            placeholder = stringResource(R.string.first_name_placeholder),
            leadingIcon = Icons.Outlined.Person,
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

        // Create Account Button
        AppButton(
            text = stringResource(R.string.create_account_btn),
            onClick = { onIntent(RegisterIntent.OnRegisterClicked) },
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
            text = stringResource(R.string.sign_up_google),
            icon = ImageVector.vectorResource(id = DesignSystemR.drawable.ic_google),
            onClick = { onIntent(RegisterIntent.OnGoogleSignUpClicked) }
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        // Log in Link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.already_have_account),
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.text.secondary
            )
            Text(
                text = stringResource(R.string.log_in),
                style = Theme.typography.label.large,
                color = Theme.colorScheme.brand.primary,
                modifier = Modifier.clickable { onIntent(RegisterIntent.OnLoginClicked) }
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xl))
    }
}
