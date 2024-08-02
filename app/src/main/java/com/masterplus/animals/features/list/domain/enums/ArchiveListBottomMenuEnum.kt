package com.masterplus.animals.features.list.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FolderDelete
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class ArchiveListBottomMenuEnum: IMenuItemEnum {

    Rename {
        override val title: UiText
            get() = UiText.Resource(R.string.rename)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Outlined.DriveFileRenameOutline)

    },
    Delete {
        override val title: UiText
            get() = UiText.Resource(R.string.delete)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.FolderDelete)

    },
    UnArchive{
        override val title: UiText
            get() = UiText.Resource(R.string.unarchive)

        override val iconInfo: IconInfo
            get() = IconInfo(imageVector = Icons.Default.Unarchive)

    };

}