package com.iti.mongez.domain.core.exception

/**
 * Custom exception mapping used by the Data layer (safeApi) to return domain-specific errors.
 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause)

class AuthException(message: String, cause: Throwable? = null) : AppException(message, cause)
class NetworkException(message: String, cause: Throwable? = null) : AppException(message, cause)
class UnknownException(message: String, cause: Throwable? = null) : AppException(message, cause)