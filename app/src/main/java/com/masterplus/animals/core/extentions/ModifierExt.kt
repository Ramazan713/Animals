package com.masterplus.animals.core.extentions

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.masterplus.animals.core.presentation.transition.LocalNavAnimatedVisibilityScope
import com.masterplus.animals.core.presentation.transition.LocalSharedTransitionScope

@Composable
fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier{
    return clickable(
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        },
        enabled = enabled,
        role = role,
        onClickLabel = onClickLabel,
        onClick = onClick
    )
}

@Composable
fun Modifier.useBackground(
    backgroundColor: Color?,
    shape: Shape = RectangleShape
): Modifier{
    if(backgroundColor == null) return this
    return this.background(backgroundColor, shape)
}

@Composable
fun Modifier.useBorder(
    borderWidth: Dp?,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    shape: Shape = RectangleShape
): Modifier{
    if(borderWidth == null) return this
    return this.border(borderWidth,color, shape)
}


fun Modifier.clearFocusOnTap(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    Modifier.pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown(pass = PointerEventPass.Initial)
            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
            if (upEvent != null) {
                focusManager.clearFocus()
            }
        }
    }
}

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
            .skipToLookaheadSize()
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

@OptIn(ExperimentalSharedTransitionApi::class)
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
