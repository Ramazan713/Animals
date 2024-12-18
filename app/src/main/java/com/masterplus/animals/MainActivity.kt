package com.masterplus.animals

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.ktx.Firebase
import com.masterplus.animals.core.presentation.utils.ListenEventLifecycle
import com.masterplus.animals.core.shared_features.translation.presentation.TranslationViewModel
import com.masterplus.animals.features.app.presentation.MyApp
import com.masterplus.animals.features.app.presentation.init.initAppCheck
import com.masterplus.animals.ui.theme.AnimalsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val translationViewModel by viewModel<TranslationViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
