package com.masterplus.animals.features.animal.data.di

import com.masterplus.animals.features.animal.data.repo.DailyAnimalRepoImpl
import com.masterplus.animals.features.animal.domain.repo.DailyAnimalRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val animalDataModule = module {
    singleOf(::DailyAnimalRepoImpl).bind<DailyAnimalRepo>()
}