package com.masterplus.animals.core.data.repo

import android.content.Context
import com.masterplus.animals.core.domain.repo.StringProvider

class StringProviderImpl(
    private val context: Context
): StringProvider {
    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}