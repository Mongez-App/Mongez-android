package com.iti.mongez.domain.core.exception

/**
 * Custom exception mapping used by the Data layer (safeApi) to return domain-specific errors.
 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause)

class AuthException(message: String) : AppException(message)
class NetworkException(message: String) : AppException(message)
class UnknownException(message: String) : AppException(message)