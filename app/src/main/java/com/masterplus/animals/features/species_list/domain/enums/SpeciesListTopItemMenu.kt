package com.masterplus.animals.features.species_list.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class SpeciesListTopItemMenu: IMenuItemEnum {
    SavePointSettings {
        override val title: UiText
            get() = UiText.Text("Kayıt Noktası Ayarları")
        override val iconInfo: IconInfo
            get() = IconInfo(Icons.Default.Settings)
    },
    Savepoint {
        override val title: UiText
            get() = UiText.Resource(R.string.save_point)
        override val iconInfo: IconInfo
            get() = IconInfo(Icons.Default.Save)
    },

}