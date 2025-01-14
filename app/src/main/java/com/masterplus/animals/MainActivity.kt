package com.masterplus.animals

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.MobileAds
import com.masterplus.animals.core.presentation.utils.ListenEventLifecycle
import com.masterplus.animals.core.shared_features.translation.presentation.TranslationViewModel
import com.masterplus.animals.features.app.presentation.AppViewModel
import com.masterplus.animals.features.app.presentation.MyApp
import com.masterplus.animals.features.app.presentation.init.initAppCheck
import com.masterplus.animals.ui.theme.AnimalsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val translationViewModel by viewModel<TranslationViewModel>()
    private val appViewModel by viewModel<AppViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        MobileAds.initialize(this) {}

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
            val appState by appViewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                translationViewModel.checkLanguageFirstTime()
            }
            AnimalsTheme(
                fontSizeEnum = appState.fontSizeEnum
            ) {
                CompositionLocalProvider(LocalOverscrollConfiguration provides null){
                    MyApp(
                        appState = appState
                    )
                }
            }
        }
    }

    private fun checkLanguage(){
        val code = AppCompatDelegate.getApplicationLocales().get(0)?.language
        translationViewModel.setLanguageByCode(code)
    }
}
