package com.masterplus.animals.core.shared_features.database.di

import androidx.room.Room
import com.masterplus.animals.core.shared_features.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            context = androidApplication(),
            klass = AppDatabase::class.java,
            name = "appDb.db"
        )
            .createFromAsset("appDb.db")
            .build()
    }
}