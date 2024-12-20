package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.core.extentions.launchPlayApp
import com.masterplus.animals.core.extentions.shareApp
import com.masterplus.animals.core.presentation.utils.ToastHelper
import com.masterplus.animals.features.settings.presentation.components.SettingItem
import com.masterplus.animals.features.settings.presentation.components.SettingSectionItem


@Composable
fun ApplicationSettingSection(){
    val context = LocalContext.current
    SettingSectionItem(
        title = stringResource(R.string.application)
    ){
        SettingItem(
            title = stringResource(R.string.share_app),
            onClick = {
                context.shareApp()
            },
            imageVector = Icons.Default.Share
        )
        SettingItem(
            title = stringResource(R.string.rate_app),
            onClick = {
                context.launchPlayApp(onError = {
                    ToastHelper.showMessage(it,context)
                })
            },
            imageVector = Icons.Default.Star
        )
    }
}