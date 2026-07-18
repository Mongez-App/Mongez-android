package com.iti.mongez.data.network


import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.core.exceptions.AppException
import com.iti.mongez.domain.core.exceptions.AuthException
import com.iti.mongez.domain.core.exceptions.NetworkException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Executes a suspended Retrofit network operation safely, translating system/framework
 * exceptions directly into your team's Domain Result layer.
 */
suspend fun <T> safeApi(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall())
    } catch (throwable: Throwable) {
        Result.Failure(throwable.toDomainException())
    }
}

/**
 * Maps raw networking throwables into your project's specific domain sealed classes.
 */
private fun Throwable.toDomainException(): AppException {
    return when (this) {
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
        is IOException -> {
            NetworkException.NoConnection()
        }
        is AppException -> {
            // If it's somehow already a domain exception, pass it right through
            this
        }
        else -> {
            AppException.UnknownException(
                message = this.message ?: "An unknown error occurred",
                cause = this
            )
        }
    }
}