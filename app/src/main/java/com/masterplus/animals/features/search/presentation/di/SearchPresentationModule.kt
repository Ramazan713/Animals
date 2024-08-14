package com.masterplus.animals.features.search.presentation.di

import com.masterplus.animals.features.search.presentation.category_search.CategorySearchViewModel
import com.masterplus.animals.features.search.presentation.category_search.CategorySpeciesSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val searchPresentationModule = module {
    viewModelOf(::CategorySearchViewModel)
    viewModelOf(::CategorySpeciesSearchViewModel)
}