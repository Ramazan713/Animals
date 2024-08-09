package com.masterplus.animals

import android.app.Application
import com.masterplus.animals.core.shared_features.database.di.databaseModule
import com.masterplus.animals.core.data.di.coreDataModule
import com.masterplus.animals.core.shared_features.auth.data.di.authDataModule
import com.masterplus.animals.core.shared_features.auth.domain.di.authDomainModule
import com.masterplus.animals.core.shared_features.auth.presentation.di.authPresentationModule
import com.masterplus.animals.core.shared_features.list.data.di.listDataModule
import com.masterplus.animals.core.shared_features.list.domain.di.listDomainModule
import com.masterplus.animals.core.shared_features.list.presentation.di.sharedListPresentationModule
import com.masterplus.animals.core.shared_features.savepoint.data.di.savePointDataModule
import com.masterplus.animals.core.shared_features.savepoint.domain.di.savePointDomainModule
import com.masterplus.animals.core.shared_features.savepoint.presentation.di.sharedSavePointPresentationModule
import com.masterplus.animals.features.animal.data.di.animalDataModule
import com.masterplus.animals.features.animal.presentation.di.animalPresentationModule
import com.masterplus.animals.features.species_detail.presentation.di.speciesDetailPresentationModule
import com.masterplus.animals.features.species_list.presentation.di.speciesListPresentationModule
import com.masterplus.animals.features.category_list.presentation.di.categoryListPresentationModule
import com.masterplus.animals.features.list.presentation.di.showListPresentationModule
import com.masterplus.animals.features.savepoints.presentation.di.savePointsPresentationModule
import com.masterplus.animals.features.settings.presentation.di.settingsPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnimalsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AnimalsApp)
            modules(
                databaseModule, animalPresentationModule, coreDataModule,
                categoryListPresentationModule, speciesListPresentationModule,
                speciesDetailPresentationModule, animalDataModule,
                listDataModule, showListPresentationModule, sharedListPresentationModule, listDomainModule,
                savePointDataModule, savePointDomainModule, sharedSavePointPresentationModule, savePointsPresentationModule,
                authDataModule, authDomainModule, authPresentationModule, settingsPresentationModule
            )
        }
    }
}