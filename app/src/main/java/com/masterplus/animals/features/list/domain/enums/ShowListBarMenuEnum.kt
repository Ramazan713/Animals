package com.masterplus.animals.features.list.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class ShowListBarMenuEnum: IMenuItemEnum {
    ShowSelectSavePoint {
        override val title: UiText
            get() = UiText.Resource(R.string.save_point)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Save)
    },
    Settings{
        override val title: UiText
            get() = UiText.Resource(R.string.settings)
        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Settings)
    }
}