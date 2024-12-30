package com.masterplus.animals.core.domain.enums

enum class OrderDirection {
    ASCENDING, DESCENDING;

    val isAscending get() = this == ASCENDING
    val isDescending get() = this == DESCENDING
}