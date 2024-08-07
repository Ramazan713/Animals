package com.masterplus.animals.features.settings.presentation.di

import com.masterplus.animals.features.settings.presentation.SettingsViewModel
import com.masterplus.animals.features.settings.presentation.link_accounts.LinkAccountsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LinkAccountsViewModel)
}