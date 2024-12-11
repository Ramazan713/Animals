package com.masterplus.animals.core.domain.repo

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes resId: Int, vararg args: Any): String
    fun getString(text: String): String
}