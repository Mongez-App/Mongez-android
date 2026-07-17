package com.iti.mongez.domain.core.exceptions

sealed class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause){
    class UnknownException(message: String = "An unknown error occurred", cause: Throwable? = null) : AppException(message,cause)
    class TooManyRequests : AppException(
        "Too many attempts. Please wait a few minutes before trying again."
    )
}
