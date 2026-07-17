package com.iti.mongez.domain.core.result

/**
 * Universal wrapper used to communicate state across the architectural boundary.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}