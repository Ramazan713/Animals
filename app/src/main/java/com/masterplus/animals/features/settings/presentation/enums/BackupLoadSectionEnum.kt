package com.masterplus.animals.features.settings.presentation.enums

import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class BackupLoadSectionEnum: IMenuItemEnum {

    LoadLastBackup {
        override val title: UiText
            get() = UiText.Resource(R.string.load_last_Backup)
    },
    ShowBackupFiles {
        override val title: UiText
            get() = UiText.Resource(R.string.show_backup_files)
    },
    NotShowAgain {
        override val title: UiText
            get() = UiText.Resource(R.string.not_show_warning_again)
    };

    override val iconInfo: IconInfo?
        get() = null

}