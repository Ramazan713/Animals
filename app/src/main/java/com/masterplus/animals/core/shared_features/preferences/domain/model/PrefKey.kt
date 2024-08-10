package com.masterplus.animals.core.shared_features.preferences.domain.model

data class PrefKey<out T>(
    val key: String,
    val default: T,
)