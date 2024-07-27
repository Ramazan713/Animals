package com.masterplus.animals.features.animal.presentation.di

import AnimalViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val animalPresentationModule = module {
    viewModelOf(::AnimalViewModel)
}