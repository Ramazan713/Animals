package com.masterplus.animals.core.domain.enums

enum class RemoteSourceType {
    DEFAULT, CACHE, SERVER;

    val isDefault get() = this == DEFAULT
    val isCache get() = this == CACHE
    val isServer get() = this == SERVER
}