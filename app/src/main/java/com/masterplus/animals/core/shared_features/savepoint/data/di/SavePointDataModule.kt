package com.masterplus.animals.core.shared_features.savepoint.data.di

import com.masterplus.animals.core.shared_features.savepoint.data.repo.SavePointPosRepoImpl
import com.masterplus.animals.core.shared_features.savepoint.data.repo.SavePointRepoImpl
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointPosRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val savePointDataModule = module {
    singleOf(::SavePointRepoImpl).bind<SavePointRepo>()
    singleOf(::SavePointPosRepoImpl).bind<SavePointPosRepo>()
}