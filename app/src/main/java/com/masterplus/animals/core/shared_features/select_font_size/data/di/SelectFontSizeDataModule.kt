package com.masterplus.animals.core.shared_features.select_font_size.data.di

import com.masterplus.animals.core.shared_features.select_font_size.data.repo.SelectFontSizeRepoImpl
import com.masterplus.animals.core.shared_features.select_font_size.domain.repo.SelectFontSizeRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val selectFontSizeDataModule = module {
    singleOf(::SelectFontSizeRepoImpl).bind<SelectFontSizeRepo>()
}