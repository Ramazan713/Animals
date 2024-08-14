package com.masterplus.animals.core.shared_features.add_species_to_list.presentation.di

import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val addSpeciesToListModule = module {
    viewModelOf(::AddSpeciesToListViewModel)
}