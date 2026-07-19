package com.iti.mongez.data.network

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.core.exceptions.AppException
import com.iti.mongez.domain.core.exceptions.AuthException
import com.iti.mongez.domain.core.exceptions.NetworkException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Executes a suspended Retrofit/Firebase operation safely, translating all framework
 * exceptions into the project's Domain Result layer.
 */
suspend fun <T> safeApi(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall())
    } catch (throwable: Throwable) {
        val domainException = throwable.toDomainException()
        Log.e("SafeApi", "Exception: ${domainException.message}", throwable)
        Result.Failure(domainException)
    }
}

/**
 * Maps raw Firebase, Retrofit, and network throwables into the project's
 * domain sealed exception classes. This is the single source of truth for
 * exception translation in the data layer.
 *
 * Firebase exceptions are caught here so the domain layer remains a
 * pure-Kotlin module with no Firebase dependency.
 */
private fun Throwable.toDomainException(): AppException {
    return when (this) {

        // --- Firebase Auth Exceptions ---
        // Wrong password OR malformed email — Firebase uses the same exception class,
        // so we inspect the errorCode to give the user a more precise message.
        is FirebaseAuthInvalidCredentialsException -> {
            if (this.errorCode == "ERROR_INVALID_EMAIL") {
                AuthException.InvalidEmailFormat()
            } else {
                AuthException.InvalidCredentials()
            }
        }

        // Account doesn't exist or has been disabled/deleted
        is FirebaseAuthInvalidUserException ->
            AuthException.UserNotFound()

        // Account already exists with the same email
        is FirebaseAuthUserCollisionException ->
            AuthException.EmailAlreadyInUse()

        // Password too short/weak during registration
        is FirebaseAuthWeakPasswordException ->
            AuthException.WeakPassword()

        // Firebase-level network failure (e.g. no DNS)
        is FirebaseNetworkException ->
            NetworkException.NoConnection()

        // Too many failed login attempts — Firebase blocks temporarily
        is FirebaseTooManyRequestsException ->
            AppException.TooManyRequests()

        // --- Retrofit / HTTP Exceptions ---
        is HttpException -> {
            when (this.code()) {
                401 -> AuthException.UnauthorizedAccess()
                429 -> AppException.TooManyRequests()
                else -> NetworkException.ServerError(
                    code = this.code(),
                    message = this.message()
                )
            }
        }

        // Generic IO / connectivity failure
        is SocketTimeoutException -> NetworkException.NoConnection()
        is IOException -> NetworkException.NoConnection()

        // Already a domain exception — pass through unchanged
        is AppException -> this

        else -> AppException.UnknownException(
            message = this.message ?: "An unexpected error occurred",
            cause = this
        )
    }
}