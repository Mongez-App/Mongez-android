package com.iti.mongez.presentation.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.iti.mongez.presentation.BuildConfig

/**
 * Encapsulates all Credential Manager / Google Identity setup logic.
 *
 * This is a plain class — no Hilt injection — because [CredentialManager.getCredential]
 * requires an Activity context to anchor its bottom sheet. Activity context is not
 * injectable into ViewModels (ViewModelComponent ≠ child of ActivityComponent).
 *
 * Instantiate with [remember] in a Composable using [LocalContext.current], then
 * call [getGoogleIdToken] from a coroutine (e.g., inside a LaunchedEffect) and
 * forward the result to the ViewModel as an intent. The ViewModel drives *when*
 * sign-in happens (via a side effect); this class owns *how*.
 */
class GoogleSignInManager(private val context: Context) {

    /**
     * Launches the Google account picker and returns the raw ID token on success.
     * Throws on any failure (user cancelled, misconfiguration, network error, etc.)
     * — callers should wrap with [runCatching] or try/catch.
     */
    suspend fun getGoogleIdToken(): String {
        val credentialManager = CredentialManager.create(context)

        // GetSignInWithGoogleOption is the specific option for explicit button taps.
        // Unlike GetGoogleIdOption (which is for "One Tap" bottom sheets), this one
        // will gracefully let the user add a Google account if none are on the device.
        val googleIdOption = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_WEB_CLIENT_ID).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = result.credential

        check(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            "Unexpected credential type: ${credential.type}"
        }

        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }
}
