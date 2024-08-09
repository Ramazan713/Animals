package com.masterplus.animals.features.species_detail.presentation.di

import com.masterplus.animals.features.species_detail.presentation.SpeciesDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val speciesDetailPresentationModule = module {
    viewModelOf(::SpeciesDetailViewModel)
}