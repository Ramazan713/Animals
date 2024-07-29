package com.masterplus.animals

import android.app.Application
import com.masterplus.animals.core.shared_features.database.di.databaseModule
import com.masterplus.animals.core.data.di.coreDataModule
import com.masterplus.animals.features.animal.presentation.di.animalPresentationModule
import com.masterplus.animals.features.bio_list.presentation.di.bioListPresentationModule
import com.masterplus.animals.features.category_list.presentation.di.categoryListPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnimalsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AnimalsApp)
            modules(
                databaseModule, animalPresentationModule, coreDataModule,
                categoryListPresentationModule, bioListPresentationModule
            )
        }
    }
}