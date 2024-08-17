package com.masterplus.animals.core.shared_features.translation.domain.enums

import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class LanguageEnum(
    override val title: UiText,
    val code: String
): IMenuItemEnum {
    Tr(
        title = UiText.Text("Türkçe"),
        code = "tr"
    ),
    En(
        title = UiText.Text("İngilizce"),
        code = "en"
    );


    val isTr get() = this == Tr
    val isEn get() = this == En

    override val iconInfo: IconInfo?
        get() = null

    companion object{
        val defaultValue = En

        fun fromCode(code: String): LanguageEnum{
            if(code.contains("tr")) return Tr
            return En
        }
    }
}