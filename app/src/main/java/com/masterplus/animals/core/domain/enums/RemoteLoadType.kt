package com.masterplus.animals.core.domain.enums

enum class RemoteLoadType {
    REFRESH, APPEND, PREPEND;

    val isRefresh get() = this == REFRESH
    val isAppend get() = this == APPEND
    val isPrepend get() = this == PREPEND

    val orderDirection get() = when(this){
        REFRESH -> OrderDirection.ASCENDING
        APPEND -> OrderDirection.ASCENDING
        PREPEND -> OrderDirection.DESCENDING
    }
}