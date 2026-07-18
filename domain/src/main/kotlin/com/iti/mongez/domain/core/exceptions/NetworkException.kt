package com.iti.mongez.domain.core.exceptions

sealed class NetworkException(message: String) : AppException(message) {
    class NoConnection : NetworkException("No internet connection")
    data class ServerError(val code: Int, override val message: String?) : NetworkException("Server error: $code - $message")
}
