package com.masterplus.animals.core.shared_features.backup.data.di

import com.masterplus.animals.core.shared_features.backup.data.manager.BackupManagerImpl
import com.masterplus.animals.core.shared_features.backup.data.repo.BackupMetaRepoImpl
import com.masterplus.animals.core.shared_features.backup.data.repo.FirebaseBackupMetaStorageService
import com.masterplus.animals.core.shared_features.backup.data.repo.LocalBackupRepoImpl
import com.masterplus.animals.core.shared_features.backup.domain.manager.BackupManager
import com.masterplus.animals.core.shared_features.backup.domain.repo.BackupMetaRepo
import com.masterplus.animals.core.shared_features.backup.domain.repo.BackupMetaStorageService
import com.masterplus.animals.core.shared_features.backup.domain.repo.LocalBackupRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val backupDataModule = module {
    singleOf(::FirebaseBackupMetaStorageService).bind<BackupMetaStorageService>()
    singleOf(::LocalBackupRepoImpl).bind<LocalBackupRepo>()
    singleOf(::BackupMetaRepoImpl).bind<BackupMetaRepo>()
    singleOf(::BackupManagerImpl).bind<BackupManager>()
}