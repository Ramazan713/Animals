package com.masterplus.animals.core.shared_features.database.di

import androidx.room.Room
import com.masterplus.animals.core.shared_features.database.AppDatabase
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

    single {
        get<AppDatabase>().categoryDao
    }

    single {
        get<AppDatabase>().animalDao
    }
}