package com.masterplus.animals.core.shared_features.translation.data.di

import com.masterplus.animals.core.shared_features.translation.data.repo.TranslationRepoImpl
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val translationDataModule = module {
    singleOf(::TranslationRepoImpl).bind<TranslationRepo>()
}