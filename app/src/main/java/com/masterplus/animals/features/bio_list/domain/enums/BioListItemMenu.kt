package com.masterplus.animals.features.bio_list.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class BioListItemMenu: IMenuItemEnum {
    Savepoint {
        override val title: UiText
            get() = UiText.Resource(R.string.save_point)
        override val iconInfo: IconInfo?
            get() = IconInfo(Icons.Default.Save)
    }
}