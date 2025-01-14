package com.masterplus.animals.core.presentation.transition

import kotlinx.serialization.Serializable

@Serializable
data class TransitionTextKey(
    val text: String,
    val textType: TransitionTextType,
    val extra: String? = null
)

enum class TransitionTextType{
    Title,
    Content,
    Description
}
