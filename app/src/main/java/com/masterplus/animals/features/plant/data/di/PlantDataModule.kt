package com.masterplus.animals.features.plant.data.di

import com.masterplus.animals.features.plant.data.repo.DailyPlantRepoImpl
import com.masterplus.animals.features.plant.domain.repo.DailyPlantRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val plantDataModule = module {
    singleOf(::DailyPlantRepoImpl).bind<DailyPlantRepo>()
}