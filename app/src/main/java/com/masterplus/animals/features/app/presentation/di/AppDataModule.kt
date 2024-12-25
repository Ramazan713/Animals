package com.masterplus.animals.features.app.presentation.di

import com.masterplus.animals.features.app.presentation.AppViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appDataModule = module {
    viewModelOf(::AppViewModel)
}