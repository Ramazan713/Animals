package com.masterplus.animals.core.domain.utils

interface Error {
}

data class ErrorText(val text: UiText): Error
