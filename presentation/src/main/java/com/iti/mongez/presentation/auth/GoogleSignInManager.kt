package com.iti.mongez.presentation.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.iti.mongez.presentation.BuildConfig

/**
 * Encapsulates legacy Google Sign-In setup logic.
 *
 * This provides a Composable that returns a launcher function `() -> Unit`.
 * When called, it launches the Google Sign-In intent. This is the legacy approach
 * which works reliably on empty emulators by automatically launching the "Add Account" flow.
 */
@Composable
fun rememberGoogleSignInLauncher(
    onResult: (Result<String>) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val token = account.idToken
                if (token != null) {
                    onResult(Result.success(token))
                } else {
                    onResult(Result.failure(Exception("Google Sign-In failed: No ID Token returned")))
                }
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        } else {
            onResult(Result.failure(Exception("Google Sign-In cancelled or failed")))
        }
    }

    return remember(launcher, context) {
        {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .requestEmail()
                .build()
            val client = GoogleSignIn.getClient(context, gso)
            launcher.launch(client.signInIntent)
        }
    }
}
