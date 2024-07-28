package com.masterplus.animals.core.data.di

import com.masterplus.animals.core.data.repo.CategoryRepoImpl
import com.masterplus.animals.core.domain.repo.CategoryRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::CategoryRepoImpl).bind<CategoryRepo>()
}