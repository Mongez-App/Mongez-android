package com.iti.mongez.presentation.utils

import com.iti.mongez.domain.core.exceptions.AppException
import com.iti.mongez.domain.core.exceptions.AuthException
import com.iti.mongez.domain.core.exceptions.NetworkException

/**
 * Converts a domain [AppException] into a concise, user-friendly message
 * suitable for display in a Snackbar, dialog, or error label.
 *
 * This extension belongs in the **presentation** layer so that display
 * copy stays decoupled from business logic. The domain layer only
 * carries developer-facing messages; this function adds the human-facing layer.
 */
fun AppException.toFriendlyMessage(): String = when (this) {

    // --- Auth errors ---
    is AuthException.InvalidCredentials ->
        "Incorrect email or password. Please try again."

    is AuthException.InvalidEmailFormat ->
        "Please enter a valid email address (e.g. name@example.com)."

    is AuthException.UserNotFound ->
        "No account found with this email. Try signing up instead."

    is AuthException.EmailAlreadyInUse ->
        "An account with this email already exists. Try logging in."

    is AuthException.WeakPassword ->
        "Password is too weak. Use at least 8 characters with numbers and symbols."

    is AuthException.EmailNotVerified ->
        "Please verify your email address before signing in."

    is AuthException.UnauthorizedAccess ->
        "Your session has expired. Please sign in again."

    // --- Network errors ---
    is NetworkException.NoConnection ->
        "No internet connection. Please check your network and try again."

    is NetworkException.ServerError ->
        "Something went wrong on our end (Error ${this.code}). Please try again later."

    // --- Generic errors ---
    is AppException.TooManyRequests ->
        "Too many attempts. Please wait a few minutes before trying again."

    is AppException.UnknownException ->
        "An unexpected error occurred. Please try again."
}
