package com.masterplus.animals.features.search.presentation.di

import com.masterplus.animals.features.search.presentation.category_search.search_category.SearchCategoryViewModel
import com.masterplus.animals.features.search.presentation.category_search.search_species.SearchSpeciesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val searchPresentationModule = module {
    viewModelOf(::SearchCategoryViewModel)
    viewModelOf(::SearchSpeciesViewModel)
}