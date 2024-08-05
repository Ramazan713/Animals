package com.masterplus.animals.core.shared_features.savepoint.presentation.di

import com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint.EditSavePointViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val sharedSavePointPresentationModule = module {
    viewModelOf(::EditSavePointViewModel)
}