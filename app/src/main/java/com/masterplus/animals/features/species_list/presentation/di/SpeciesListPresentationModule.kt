package com.masterplus.animals.features.species_list.presentation.di

import com.masterplus.animals.features.species_list.presentation.SpeciesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val speciesListPresentationModule = module {
    viewModelOf(::SpeciesListViewModel)
}