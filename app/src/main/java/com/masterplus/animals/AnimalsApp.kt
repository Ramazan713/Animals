package com.masterplus.animals

import android.app.Application
import com.masterplus.animals.core.shared_features.database.di.databaseModule
import com.masterplus.animals.core.data.di.coreDataModule
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.di.addSpeciesToListModule
import com.masterplus.animals.core.shared_features.auth.data.di.authDataModule
import com.masterplus.animals.core.shared_features.auth.domain.di.authDomainModule
import com.masterplus.animals.core.shared_features.auth.presentation.di.authPresentationModule
import com.masterplus.animals.core.shared_features.list.data.di.listDataModule
import com.masterplus.animals.core.shared_features.list.domain.di.listDomainModule
import com.masterplus.animals.core.shared_features.list.presentation.di.sharedListPresentationModule
import com.masterplus.animals.core.shared_features.preferences.data.di.preferencesDataModule
import com.masterplus.animals.core.shared_features.savepoint.data.di.savePointDataModule
import com.masterplus.animals.core.shared_features.savepoint.domain.di.savePointDomainModule
import com.masterplus.animals.core.shared_features.savepoint.presentation.di.sharedSavePointPresentationModule
import com.masterplus.animals.core.shared_features.theme.data.di.themeDataModule
import com.masterplus.animals.core.shared_features.theme.presentation.di.themePresentationModule
import com.masterplus.animals.core.shared_features.translation.data.di.translationDataModule
import com.masterplus.animals.core.shared_features.translation.presentation.di.translationPresentationModule
import com.masterplus.animals.features.animal.data.di.animalDataModule
import com.masterplus.animals.features.animal.presentation.di.animalPresentationModule
import com.masterplus.animals.features.species_detail.presentation.di.speciesDetailPresentationModule
import com.masterplus.animals.features.species_list.presentation.di.speciesListPresentationModule
import com.masterplus.animals.features.category_list.presentation.di.categoryListPresentationModule
import com.masterplus.animals.features.list.presentation.di.showListPresentationModule
import com.masterplus.animals.features.savepoints.presentation.di.savePointsPresentationModule
import com.masterplus.animals.features.search.data.di.searchDataModule
import com.masterplus.animals.features.search.domain.di.searchDomainModule
import com.masterplus.animals.features.search.presentation.di.searchPresentationModule
import com.masterplus.animals.features.settings.presentation.di.settingsPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnimalsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AnimalsApp)
            modules(
                databaseModule, animalPresentationModule, coreDataModule, themeDataModule, themePresentationModule,
                categoryListPresentationModule, speciesListPresentationModule, addSpeciesToListModule,
                speciesDetailPresentationModule, animalDataModule, preferencesDataModule,
                listDataModule, showListPresentationModule, sharedListPresentationModule, listDomainModule,
                savePointDataModule, savePointDomainModule, sharedSavePointPresentationModule, savePointsPresentationModule,
                authDataModule, authDomainModule, authPresentationModule, settingsPresentationModule,
                searchPresentationModule, searchDataModule, searchDomainModule,
                translationDataModule, translationPresentationModule
            )
        }
    }
}