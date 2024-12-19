package com.masterplus.animals.core.shared_features.backup.presentation.di

import com.masterplus.animals.core.shared_features.backup.presentation.backup_select.CloudSelectBackupViewModel
import com.masterplus.animals.core.shared_features.backup.presentation.cloud_backup.CloudBackupViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val backupPresentationModule = module {
    viewModelOf(::CloudBackupViewModel)
    viewModelOf(::CloudSelectBackupViewModel)
}