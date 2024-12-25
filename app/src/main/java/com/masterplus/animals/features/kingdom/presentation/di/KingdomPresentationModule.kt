package com.masterplus.animals.features.kingdom.presentation.di

import com.masterplus.animals.features.kingdom.presentation.AnimalViewModel
import com.masterplus.animals.features.kingdom.presentation.PlantViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val kingdomPresentationModule = module {
    viewModelOf(::AnimalViewModel)
    viewModelOf(::PlantViewModel)
}