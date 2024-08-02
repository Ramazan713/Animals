package com.masterplus.animals.features.list.presentation.di

import com.masterplus.animals.features.list.presentation.archive_list.ArchiveListViewModel
import com.masterplus.animals.features.list.presentation.show_list.ShowListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val showListPresentationModule = module {
    viewModelOf(::ShowListViewModel)
    viewModelOf(::ArchiveListViewModel)
}