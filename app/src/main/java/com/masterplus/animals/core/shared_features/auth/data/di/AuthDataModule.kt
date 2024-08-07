package com.masterplus.animals.core.shared_features.auth.data.di

import com.masterplus.animals.core.shared_features.auth.data.repo.FirebaseAuthRepo
import com.masterplus.animals.core.shared_features.auth.domain.repo.AuthRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::FirebaseAuthRepo).bind<AuthRepo>()
}