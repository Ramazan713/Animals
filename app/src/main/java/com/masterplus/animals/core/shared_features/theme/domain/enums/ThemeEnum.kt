package com.masterplus.animals.core.shared_features.theme.domain.enums

import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.preferences.domain.model.IEnumPrefValue


enum class ThemeEnum(
    override val title: UiText,
    override val keyValue: Int
): IEnumPrefValue, IMenuItemEnum {
    System(
        title = UiText.Resource(R.string.system),
        keyValue = 1
    ) {
        override fun hasDarkTheme(useSystemDark: Boolean): Boolean {
            return useSystemDark
        }

        override val iconInfo: IconInfo?
            get() = null
    },
    Light(
        title = UiText.Resource(R.string.light),
        keyValue = 2
    ) {
        override fun hasDarkTheme(useSystemDark: Boolean): Boolean {
            return false
        }
        override val iconInfo: IconInfo?
            get() = null
    },
    Dark(
        title = UiText.Resource(R.string.dark),
        keyValue = 3
    ) {
        override fun hasDarkTheme(useSystemDark: Boolean): Boolean {
            return true
        }
        override val iconInfo: IconInfo?
            get() = null
    };

    abstract fun hasDarkTheme(useSystemDark: Boolean):Boolean

    companion object{

        val defaultValue = System

        fun fromKeyValue(keyValue: Int): ThemeEnum {
            return when(keyValue){
                1-> System
                2-> Light
                3-> Dark
                else -> defaultValue
            }
        }
    }
}