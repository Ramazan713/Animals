package com.masterplus.animals.core.shared_features.theme.presentation.di

import com.masterplus.animals.core.shared_features.theme.presentation.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val themePresentationModule = module {
    viewModelOf(::ThemeViewModel)
}