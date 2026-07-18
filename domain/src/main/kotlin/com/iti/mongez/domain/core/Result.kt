package com.iti.mongez.domain.core

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    inline fun <R> fold(
        onSuccess: (T) -> R,
        onFailure: (Throwable) -> R,
        onLoading: () -> R
    ): R = when (this) {
        is Success -> onSuccess(data)
        is Failure -> onFailure(exception)
        is Loading -> onLoading()
    }
}
