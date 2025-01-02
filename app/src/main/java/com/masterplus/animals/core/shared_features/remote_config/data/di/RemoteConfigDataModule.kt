package com.masterplus.animals.core.shared_features.remote_config.data.di

import com.masterplus.animals.core.shared_features.remote_config.data.repo.RemoteConfigRepoImpl
import com.masterplus.animals.core.shared_features.remote_config.domain.repo.RemoteConfigRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteConfigDataModule = module {
    singleOf(::RemoteConfigRepoImpl).bind<RemoteConfigRepo>()
}