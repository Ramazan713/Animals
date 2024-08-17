package com.masterplus.animals.features.search.data.di

import com.masterplus.animals.features.search.data.repo.HistoryRepoImpl
import com.masterplus.animals.features.search.data.repo.SearchRepoImpl
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
    singleOf(::SearchRepoImpl).bind<SearchRepo>()
    singleOf(::HistoryRepoImpl).bind<HistoryRepo>()
}