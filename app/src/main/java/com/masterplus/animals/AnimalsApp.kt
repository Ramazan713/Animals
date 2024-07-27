package com.masterplus.animals

import android.app.Application
import com.masterplus.animals.core.shared_features.database.di.databaseModule
import com.masterplus.animals.features.animal.data.di.animalDataModule
import com.masterplus.animals.features.animal.presentation.di.animalPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnimalsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AnimalsApp)
            modules(
                databaseModule, animalPresentationModule, animalDataModule
            )
        }
    }
}