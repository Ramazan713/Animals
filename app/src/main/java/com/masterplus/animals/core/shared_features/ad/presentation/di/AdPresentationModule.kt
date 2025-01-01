package com.masterplus.animals.core.shared_features.ad.presentation.di

import com.masterplus.animals.core.shared_features.ad.presentation.AdViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val adPresentationModule = module {
    viewModelOf(::AdViewModel)
}