package com.iti.mongez.domain.core.exceptions

sealed class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)
