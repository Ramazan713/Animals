package com.masterplus.animals

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.ktx.Firebase
import com.masterplus.animals.core.data.di.coreDataModule
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.di.addSpeciesToListModule
import com.masterplus.animals.core.shared_features.analytics.data.di.analyticsDataModule
import com.masterplus.animals.core.shared_features.auth.data.di.authDataModule
import com.masterplus.animals.core.shared_features.auth.domain.di.authDomainModule
import com.masterplus.animals.core.shared_features.auth.presentation.di.authPresentationModule
import com.masterplus.animals.core.shared_features.backup.data.di.backupDataModule
import com.masterplus.animals.core.shared_features.backup.presentation.di.backupPresentationModule
import com.masterplus.animals.core.shared_features.database.di.databaseModule
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
import com.masterplus.animals.features.app.presentation.di.appDataModule
import com.masterplus.animals.features.category_list.presentation.di.categoryListPresentationModule
import com.masterplus.animals.features.kingdom.data.di.kingdomDataModule
import com.masterplus.animals.features.kingdom.presentation.di.kingdomPresentationModule
import com.masterplus.animals.features.list.presentation.di.showListPresentationModule
import com.masterplus.animals.features.savepoints.presentation.di.savePointsPresentationModule
import com.masterplus.animals.features.search.data.di.searchDataModule
import com.masterplus.animals.features.search.domain.di.searchDomainModule
import com.masterplus.animals.features.search.presentation.di.searchPresentationModule
import com.masterplus.animals.features.settings.presentation.di.settingsPresentationModule
import com.masterplus.animals.features.species_detail.presentation.di.speciesDetailPresentationModule
import com.masterplus.animals.features.species_list.presentation.di.speciesListPresentationModule
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.File

class AnimalsApp: Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AnimalsApp)
            modules(
                databaseModule,  coreDataModule, themeDataModule, themePresentationModule,
                categoryListPresentationModule, speciesListPresentationModule, addSpeciesToListModule,
                speciesDetailPresentationModule, preferencesDataModule, kingdomPresentationModule, kingdomDataModule,
                listDataModule, showListPresentationModule, sharedListPresentationModule, listDomainModule,
                savePointDataModule, savePointDomainModule, sharedSavePointPresentationModule, savePointsPresentationModule,
                authDataModule, authDomainModule, authPresentationModule, settingsPresentationModule,
                searchPresentationModule, searchDataModule, searchDomainModule,
                translationDataModule, translationPresentationModule,
                backupDataModule, backupPresentationModule, analyticsDataModule, appDataModule
            )
        }
    }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val token = runBlocking {
                try{
                    Firebase.appCheck.getAppCheckToken(false).await().token
                }catch (e: Exception){ "" }
            }
            val newRequest = chain.request().newBuilder().apply {
                addHeader("X-Firebase-AppCheck", token)
            }.build()
            chain.proceed(newRequest)
        }
    }.build()

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader(context).newBuilder()
            .components { add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient })) }
            .diskCache {
                DiskCache.Builder()
                    .directory(File(cacheDir, "image_cache"))
                    .maxSizeBytes(100 * 1024 * 1024)
                    .build()
            }
            .memoryCache {
                MemoryCache.Builder()
                    .strongReferencesEnabled(true)
                    .maxSizeBytes(20*1024*1024)
                    .build()
            }
            .apply {
//                if(BuildConfig.DEBUG){
//                    logger(DebugLogger())
//                }
            }
            .build()
    }
}