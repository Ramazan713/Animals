package com.masterplus.animals.core.shared_features.theme.data.di

import com.masterplus.animals.core.shared_features.theme.data.repo.ThemeRepoImpl
import com.masterplus.animals.core.shared_features.theme.domain.repo.ThemeRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val themeDataModule = module {
    singleOf(::ThemeRepoImpl).bind<ThemeRepo>()
}