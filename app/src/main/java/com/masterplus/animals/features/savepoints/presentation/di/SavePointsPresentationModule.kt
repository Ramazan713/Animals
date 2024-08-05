package com.masterplus.animals.features.savepoints.presentation.di

import com.masterplus.animals.features.savepoints.presentation.show_savepoints.ShowSavePointsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val savePointsPresentationModule = module {
    viewModelOf(::ShowSavePointsViewModel)
}