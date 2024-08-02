package com.masterplus.animals.core.shared_features.list.data.di

import com.masterplus.animals.core.shared_features.list.data.repo.ListAnimalsRepoImpl
import com.masterplus.animals.core.shared_features.list.data.repo.ListRepoImpl
import com.masterplus.animals.core.shared_features.list.data.repo.ListViewRepoImpl
import com.masterplus.animals.core.shared_features.list.domain.repo.ListAnimalsRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListViewRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val listDataModule = module {
    singleOf(::ListRepoImpl).bind<ListRepo>()
    singleOf(::ListViewRepoImpl).bind<ListViewRepo>()
    singleOf(::ListAnimalsRepoImpl).bind<ListAnimalsRepo>()
}