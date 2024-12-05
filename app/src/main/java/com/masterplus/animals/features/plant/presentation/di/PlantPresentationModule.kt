package com.masterplus.animals.features.plant.presentation.di

import PlantViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val plantPresentationModule = module {
    viewModelOf(::PlantViewModel)
}