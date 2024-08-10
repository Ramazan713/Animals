package com.masterplus.animals.core.shared_features.preferences.domain.model

data class EnumPrefKey<E:Enum<E>>(
    val key: String,
    val default: IEnumPrefValue,
    val from: (Int)-> E
)