package com.masterplus.animals.features.category_list.presentation.di

import com.masterplus.animals.features.category_list.presentation.CategoryListViewModel
import com.masterplus.animals.features.category_list.presentation.CategoryListWithDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val categoryListPresentationModule = module {
    viewModelOf(::CategoryListViewModel)
    viewModelOf(::CategoryListWithDetailViewModel)
}