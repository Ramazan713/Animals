package com.masterplus.animals.core.domain.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.core.domain.repo.StringProvider


sealed class UiText{
    data class Resource(
        @StringRes val resId: Int,
        val formatArgs: List<Any> = emptyList()
    ): UiText()

    data class Text(val content: String): UiText()


    fun asString(context: Context): String{
        return when(this){
            is Resource -> context.getString(resId,*formatArgs.toTypedArray())
            is Text -> content
        }
    }

    fun asString(stringProvider: StringProvider): String{
        return when(this){
            is Resource -> stringProvider.getString(resId,*formatArgs.toTypedArray())
            is Text -> stringProvider.getString(content)
        }
    }


    @Composable
    fun asString(): String{
        return when(this){
            is Resource -> stringResource(resId,*formatArgs.toTypedArray())
            is Text -> content
        }
    }
}