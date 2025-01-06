package com.masterplus.animals.core.shared_features.select_font_size.presentation.di

import com.masterplus.animals.core.shared_features.select_font_size.presentation.SelectFontSizeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val selectFontSizePresentationModule = module {
    viewModelOf(::SelectFontSizeViewModel)
}