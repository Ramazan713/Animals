package com.masterplus.animals.core.data.di

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.datasources.FirebaseCategoryRemoteSource
import com.masterplus.animals.core.data.helpers.InsertFirebaseSpeciesHelper
import com.masterplus.animals.core.data.mediators.RemoteMediatorConfig
import com.masterplus.animals.core.data.repo.AnimalRepoImpl
import com.masterplus.animals.core.data.repo.CategoryRepoImpl
import com.masterplus.animals.core.data.repo.ConnectivityObserverImpl
import com.masterplus.animals.core.data.repo.FirebaseCategoryRemoteRepo
import com.masterplus.animals.core.data.repo.PlantRepoImpl
import com.masterplus.animals.core.data.repo.SpeciesRepoImpl
import com.masterplus.animals.core.data.repo.StringProviderImpl
import com.masterplus.animals.core.data.services.CheckDayChangeServiceImpl
import com.masterplus.animals.core.domain.repo.AnimalRepo
import com.masterplus.animals.core.domain.repo.CategoryRemoteRepo
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.repo.ConnectivityObserver
import com.masterplus.animals.core.domain.repo.PlantRepo
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.domain.repo.StringProvider
import com.masterplus.animals.core.domain.services.CheckDayChangeService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::CategoryRepoImpl).bind<CategoryRepo>()
    singleOf(::AnimalRepoImpl).bind<AnimalRepo>()
    singleOf(::SpeciesRepoImpl).bind<SpeciesRepo>()
    singleOf(::PlantRepoImpl).bind<PlantRepo>()
    singleOf(::StringProviderImpl).bind<StringProvider>()
    singleOf(::FirebaseCategoryRemoteSource).bind<CategoryRemoteSource>()
    singleOf(::FirebaseCategoryRemoteRepo).bind<CategoryRemoteRepo>()
    singleOf(::ConnectivityObserverImpl).bind<ConnectivityObserver>()
    singleOf(::RemoteMediatorConfig)
    singleOf(::InsertFirebaseSpeciesHelper)
    singleOf(::CheckDayChangeServiceImpl).bind<CheckDayChangeService>()
}