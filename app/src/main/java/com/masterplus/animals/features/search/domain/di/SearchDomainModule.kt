package com.masterplus.animals.features.search.domain.di

import com.masterplus.animals.features.search.domain.use_cases.GetSearchQueryUseCase
import org.koin.dsl.module

val searchDomainModule = module {
    single {
        GetSearchQueryUseCase()
    }
}