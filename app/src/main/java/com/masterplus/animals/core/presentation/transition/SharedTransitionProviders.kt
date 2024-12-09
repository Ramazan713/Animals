package com.masterplus.animals.core.presentation.transition

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }


@Composable
fun NavAnimatedVisibilityProvider(
    scope: AnimatedContentScope,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalNavAnimatedVisibilityScope provides scope,
    ){
        content()
    }
}