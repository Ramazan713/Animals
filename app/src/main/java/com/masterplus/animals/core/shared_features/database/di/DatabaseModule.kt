package com.masterplus.animals.core.shared_features.database.di

import androidx.room.Room
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.TransactionProvider
import com.masterplus.animals.core.shared_features.database.TransactionProviderImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "appDB.db"
        )
            .createFromAsset("appDB.db")
            .build()
    }

    single { get<AppDatabase>().categoryDao }

    single { get<AppDatabase>().animalDao }

    single { get<AppDatabase>().plantDao }

    single { get<AppDatabase>().listDao }

    single { get<AppDatabase>().listViewDao }

    single { get<AppDatabase>().listSpeciesDao }

    single { get<AppDatabase>().savePointDao }

    single { get<AppDatabase>().savePointPosDao }

    single { get<AppDatabase>().speciesDao }

    single { get<AppDatabase>().searchCategoryDao }

    single { get<AppDatabase>().searchSpeciesDao }

    single { get<AppDatabase>().historyDao }

    single { get<AppDatabase>().backupMetaDao }

    single { get<AppDatabase>().backupDao }

    single { get<AppDatabase>().remoteKeyDao }

    singleOf(::TransactionProviderImpl).bind<TransactionProvider>()
}