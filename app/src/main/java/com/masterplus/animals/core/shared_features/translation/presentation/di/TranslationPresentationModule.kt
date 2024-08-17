package com.masterplus.animals.core.shared_features.translation.presentation.di

import com.masterplus.animals.core.shared_features.translation.presentation.TranslationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val translationPresentationModule = module {
    viewModelOf(::TranslationViewModel)
}