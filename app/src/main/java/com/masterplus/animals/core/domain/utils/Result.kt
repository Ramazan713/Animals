package com.masterplus.animals.core.domain.utils


sealed interface Result<out D, out E: Error> {

    data class Success<out D>(val data: D): Result<D, Nothing>

    data class Error<out E: com.masterplus.animals.core.domain.utils.Error>(val error: E):
        Result<Nothing, E>

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    val getSuccessData: D?
        get() = if (this is Success) this.data else null

    val getFailureError: E?
        get() = if (this is Error) this.error else null
}

inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when(this){
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E: Error> Result<T, E>.asEmptyResult(): EmptyResult<E> {
    return map {  }
}




typealias EmptyResult<E> = Result<Unit, E>

typealias DefaultResult<T> = Result<T, ErrorText>