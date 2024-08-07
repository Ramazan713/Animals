package com.masterplus.animals.core.shared_features.auth.presentation.di

import com.masterplus.animals.core.shared_features.auth.presentation.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::AuthViewModel)
}