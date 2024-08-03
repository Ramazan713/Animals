package com.masterplus.animals.core.shared_features.list.presentation.di

import com.masterplus.animals.core.shared_features.list.presentation.select_list.SelectListViewModel
import com.masterplus.animals.core.shared_features.list.presentation.select_list_with_menu.SelectListMenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val sharedListPresentationModule = module {
    viewModelOf(::SelectListMenuViewModel)
    viewModelOf(::SelectListViewModel)
}