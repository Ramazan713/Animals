package com.masterplus.animals

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.coroutineScope
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.presentation.utils.ListenEventLifecycle
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.translation.presentation.TranslationViewModel
import com.masterplus.animals.features.app.presentation.MyApp
import com.masterplus.animals.features.app.presentation.init.initAppCheck
import com.masterplus.animals.ui.theme.AnimalsTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.koinInject

class MainActivity : AppCompatActivity() {
    private val translationViewModel by viewModel<TranslationViewModel>()

    private val readCounter by inject<ServerReadCounter>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        MobileAds.initialize(this) {}

        if(BuildConfig.DEBUG){
            lifecycle.coroutineScope.launch {
                readCounter.resetCounter(ContentType.Content)
            }
        }


        initAppCheck(this)

        setContent {
            ListenEventLifecycle(
                onCreate = {
                    checkLanguage()
                },
                onResume = {
                    checkLanguage()
                }
            )
            AnimalsTheme {
                CompositionLocalProvider(LocalOverscrollConfiguration provides null){
                    MyApp()
                }
            }
        }
    }

    private fun checkLanguage(){
        val code = AppCompatDelegate.getApplicationLocales().get(0)?.language
        translationViewModel.setLanguageByCode(code)
    }
}
