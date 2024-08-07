package com.masterplus.animals.core.domain.utils

import com.masterplus.animals.R
import kotlin.coroutines.cancellation.CancellationException


suspend inline fun <reified T> safeCall(
    crossinline execute: suspend () -> T,
): Result<T, ErrorText>{
    return safeCall(
        onException = {e ->
            val error = e.localizedMessage?.let { UiText.Text(it) } ?: UiText.Resource(R.string.something_went_wrong)
            Result.Error(ErrorText(error))
        },
        execute = execute
    )
}


suspend inline fun <reified T, E: Error> safeCall(
    crossinline execute: suspend () -> T,
    onException: ((e: Exception) -> Result<T, E>),
): Result<T, E>{
    return try {
        val result = execute()
        return Result.Success(result)
    }
    catch (e: CancellationException){
        throw e
    }
    catch (e: Exception){
        onException(e)
    }
}