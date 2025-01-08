package com.masterplus.animals.core.shared_features.ad.presentation.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R

@Composable
fun ShowAdRequiredDia(
    onDismiss: () -> Unit,
    onApproved: () -> Unit,
    title: String = "",
    content: String = "Devam etmek için reklam izlemeniz gerekmektedir"
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(Icons.Default.AdsClick, contentDescription = null)
        },
        title = {
            if(title.isNotBlank()){
                Text(title)
            }
        },
        text = {
            if(content.isNotBlank()){
                Text(content)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ){
                Text(stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    onApproved()
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.approve))
            }
        }
    )
}