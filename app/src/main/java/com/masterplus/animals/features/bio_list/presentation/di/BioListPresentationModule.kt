package com.masterplus.animals.features.bio_list.presentation.di

import com.masterplus.animals.features.bio_list.presentation.BioListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val bioListPresentationModule = module {
    viewModelOf(::BioListViewModel)
}