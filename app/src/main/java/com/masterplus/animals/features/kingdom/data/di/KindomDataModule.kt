package com.masterplus.animals.features.kingdom.data.di

import com.masterplus.animals.features.kingdom.data.repo.DailySpeciesRepoImpl
import com.masterplus.animals.features.kingdom.domain.repo.DailySpeciesRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val kingdomDataModule = module {
    singleOf(::DailySpeciesRepoImpl).bind<DailySpeciesRepo>()
}