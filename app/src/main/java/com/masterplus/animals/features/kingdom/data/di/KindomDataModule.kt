package com.masterplus.animals.features.kingdom.data.di

import com.masterplus.animals.features.kingdom.data.repo.DailyAnimalRepoImpl
import com.masterplus.animals.features.kingdom.data.repo.DailyPlantRepoImpl
import com.masterplus.animals.features.kingdom.domain.repo.DailyAnimalRepo
import com.masterplus.animals.features.kingdom.domain.repo.DailyPlantRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val kingdomDataModule = module {
    singleOf(::DailyPlantRepoImpl).bind<DailyPlantRepo>()
    singleOf(::DailyAnimalRepoImpl).bind<DailyAnimalRepo>()
}