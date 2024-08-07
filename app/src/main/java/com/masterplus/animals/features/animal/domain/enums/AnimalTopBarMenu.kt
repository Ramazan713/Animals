package com.masterplus.animals.features.animal.domain.enums

import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class AnimalTopBarMenu: IMenuItemEnum {
    Settings {
        override val title: UiText
            get() = UiText.Resource(R.string.settings)
        override val iconInfo: IconInfo?
            get() = null
    }
}