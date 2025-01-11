package com.masterplus.animals.features.app.di

import com.masterplus.animals.AnimalsApp
import com.masterplus.animals.features.app.presentation.AppViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appDataModule = module {
    viewModelOf(::AppViewModel)
    single<CoroutineScope> {
        (androidApplication() as AnimalsApp).applicationScope
    }
}