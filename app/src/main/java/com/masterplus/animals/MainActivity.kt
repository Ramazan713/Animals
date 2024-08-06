package com.masterplus.animals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import com.masterplus.animals.features.app.presentation.MyApp
import com.masterplus.animals.ui.theme.AnimalsTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AnimalsTheme {
                CompositionLocalProvider(LocalOverscrollConfiguration provides null){
                    MyApp()
                }
            }
        }
    }
}
