package com.masterplus.animals.features.bio_detail.presentation.di

import com.masterplus.animals.features.bio_detail.presentation.BioDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val bioDetailPresentationModule = module {
    viewModelOf(::BioDetailViewModel)
}