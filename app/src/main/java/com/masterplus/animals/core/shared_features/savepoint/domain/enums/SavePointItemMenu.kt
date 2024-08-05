package com.masterplus.animals.core.shared_features.savepoint.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiColor
import com.masterplus.animals.core.domain.utils.UiText

enum class SavePointItemMenu: IMenuItemEnum {
    Rename {
        override val title: UiText
            get() = UiText.Text("Yeniden İsimlendir")
        override val iconInfo: IconInfo?
            get() = IconInfo(imageVector = Icons.Default.Edit)
    },
    Delete {
        override val title: UiText
            get() = UiText.Text("Sil")
        override val iconInfo: IconInfo?
            get() = IconInfo(imageVector = Icons.Default.DeleteForever, tintColor = UiColor.ComposeColor { MaterialTheme.colorScheme.error })
    }
}