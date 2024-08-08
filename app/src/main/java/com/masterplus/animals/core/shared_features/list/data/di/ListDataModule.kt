package com.masterplus.animals.core.shared_features.list.data.di

import com.masterplus.animals.core.shared_features.list.data.repo.ListSpeciesRepoImpl
import com.masterplus.animals.core.shared_features.list.data.repo.ListRepoImpl
import com.masterplus.animals.core.shared_features.list.data.repo.ListViewRepoImpl
import com.masterplus.animals.core.shared_features.list.domain.repo.ListSpeciesRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListViewRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val listDataModule = module {
    singleOf(::ListRepoImpl).bind<ListRepo>()
    singleOf(::ListViewRepoImpl).bind<ListViewRepo>()
    singleOf(::ListSpeciesRepoImpl).bind<ListSpeciesRepo>()
}