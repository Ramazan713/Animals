package com.masterplus.animals.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel
import com.masterplus.animals.core.shared_features.theme.presentation.ThemeViewModel
import com.masterplus.animals.core.shared_features.theme.presentation.extensions.getLightThemeScheme
import com.masterplus.animals.core.shared_features.theme.presentation.extensions.getThemeScheme
import org.koin.androidx.compose.koinViewModel

internal val LocalLightColorScheme = staticCompositionLocalOf { lightColorScheme() }

val MaterialTheme.lightColorScheme: ColorScheme
    @Composable
    get() = LocalLightColorScheme.current


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


@Composable
fun AnimalsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeViewModel: ThemeViewModel = koinViewModel(),
    content: @Composable () -> Unit
){
    val state by themeViewModel.state.collectAsState()

    AnimalsTheme(
        darkTheme = darkTheme,
        state = state,
        content = content
    )
}

@Composable
fun AnimalsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    state: ThemeModel,
    content: @Composable () -> Unit
) {
    val colorScheme = state.getThemeScheme(darkColorScheme = DarkColorScheme, lightColorScheme = LightColorScheme)
    val lightColorScheme = state.getLightThemeScheme(lightColorScheme = LightColorScheme)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as? Activity)?.window?.let { currentWindow->
                currentWindow.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars = state.themeEnum.hasDarkTheme(darkTheme)

            }
        }
    }

    CompositionLocalProvider(
        LocalLightColorScheme provides lightColorScheme,
    ){
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}