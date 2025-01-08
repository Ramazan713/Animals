package com.masterplus.animals.features.search.data.di

import com.masterplus.animals.features.search.data.repo.HistoryRepoImpl
import com.masterplus.animals.features.search.data.repo.SearchRemoteRepoImpl
import com.masterplus.animals.features.search.data.repo.SearchRepoImpl
import com.masterplus.animals.features.search.data.service.AlgoliaSearchSpeciesIndexService
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import com.masterplus.animals.features.search.domain.repo.SearchRemoteRepo
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import com.masterplus.animals.features.search.domain.service.SearchSpeciesIndexService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchDataModule = module {
    singleOf(::SearchRepoImpl).bind<SearchRepo>()
    singleOf(::HistoryRepoImpl).bind<HistoryRepo>()
    singleOf(::AlgoliaSearchSpeciesIndexService).bind<SearchSpeciesIndexService>()
    singleOf(::SearchRemoteRepoImpl).bind<SearchRemoteRepo>()
}