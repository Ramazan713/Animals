package com.masterplus.animals.core.presentation.transition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier



@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedBoundsText(
    scope: SharedTransitionScope,
    contentStateKey: String,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    enabled: Boolean = true
): Modifier {
    val currentAnimatedVisibilityScope = animatedVisibilityScope ?: LocalNavAnimatedVisibilityScope.current
    if(currentAnimatedVisibilityScope == null ||  contentStateKey == "" || !enabled) return Modifier
    return with(scope){
        Modifier
            .sharedBounds(
                rememberSharedContentState(contentStateKey),
                animatedVisibilityScope = currentAnimatedVisibilityScope,
                enter = fadeIn(),
                exit = fadeOut(),
            )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.sharedBoundsText(
    contentStateKey: String,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    enabled: Boolean = true
): Modifier {
    val sharedTransitionScope = LocalSharedTransitionScope.current ?: return Modifier
    return Modifier.sharedBoundsText(
        scope = sharedTransitionScope,
        contentStateKey = contentStateKey,
        animatedVisibilityScope = animatedVisibilityScope,
        enabled = enabled
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.renderInSharedTransitionScopeOverlayDefault(
    sharedTransitionScope: SharedTransitionScope? = null
): Modifier{
    val currentSharedTransitionScope = sharedTransitionScope ?: LocalSharedTransitionScope.current
    if(currentSharedTransitionScope == null) return Modifier
    with(currentSharedTransitionScope){
        return Modifier
            .renderInSharedTransitionScopeOverlay(
                zIndexInOverlay = 1f,
            )
    }
}

@Composable
fun Modifier.animateEnterExitForTransition(
    enter: EnterTransition? = null,
    exit: ExitTransition? = null,
    offsetY: (fullHeight: Int) -> Int = { -it },
    animatedVisibilityScope: AnimatedVisibilityScope? = null
): Modifier{
    val currentAnimatedVisibilityScope = animatedVisibilityScope ?: LocalNavAnimatedVisibilityScope.current
    if(currentAnimatedVisibilityScope == null) return Modifier
    with(currentAnimatedVisibilityScope){
        return Modifier
            .animateEnterExit(
                enter = enter ?: (fadeIn() + slideInVertically(initialOffsetY = offsetY)),
                exit = exit ?: (fadeOut() + slideOutVertically(targetOffsetY = offsetY))
            )
    }
}
