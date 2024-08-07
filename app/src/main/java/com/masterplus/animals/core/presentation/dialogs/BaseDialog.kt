package com.masterplus.animals.core.presentation.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun BaseDialog(
    onClosed: () -> Unit,
    modifier: Modifier = Modifier,
    allowDismiss: Boolean = true,
    usePlatformDefaultWidth: Boolean = false,
    content: @Composable () -> Unit,
){

    Dialog(
        onDismissRequest = onClosed,
        properties = DialogProperties(
            usePlatformDefaultWidth = usePlatformDefaultWidth,
            dismissOnBackPress = allowDismiss,
            dismissOnClickOutside = allowDismiss
        )
    ){
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(MaterialTheme.shapes.medium),
            color = MaterialTheme.colorScheme.surface
        ){
            content()
        }
    }
}