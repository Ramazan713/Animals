package com.masterplus.animals.core.shared_features.list.domain.di

import com.masterplus.animals.core.shared_features.list.domain.use_cases.ListInFavoriteControlForDeletionUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val listDomainModule = module {
    singleOf(::ListInFavoriteControlForDeletionUseCase)
}