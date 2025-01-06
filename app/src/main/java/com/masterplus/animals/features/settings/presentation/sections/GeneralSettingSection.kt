package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.SettingsState
import com.masterplus.animals.features.settings.presentation.components.SettingItem
import com.masterplus.animals.features.settings.presentation.components.SettingSectionItem
import com.masterplus.animals.features.settings.presentation.components.SwitchItem


@Composable
fun GeneralSettingSection(
    state: SettingsState,
    onAction: (SettingsAction)->Unit,
){
    SettingSectionItem(
        title = stringResource(R.string.general_setting)
    ){
        SettingItem(
            title = stringResource(R.string.theme_mode),
            subTitle = state.themeModel.themeEnum.title.asString(),
            onClick = {
                onAction(
                    SettingsAction.ShowDialog(SettingsDialogEvent.ShowSelectTheme))
            },
            imageVector = Icons.Default.Palette,
        )
        SettingItem(
            title = "Dil Seç",
            subTitle = state.language.title.asString(),
            onClick = {
                onAction(
                    SettingsAction.ShowDialog(SettingsDialogEvent.ShowSelectLanguage))
            },
            imageVector = Icons.Default.Language,
        )

        SettingItem(
            title = "Yazı Boyutu",
            subTitle = state.fontSizeEnum.description.asString(),
            onClick = {
                onAction(SettingsAction.ShowDialog(SettingsDialogEvent.SelectFontSize))
            },
            imageVector = Icons.Default.FontDownload,
        )

        if(state.themeModel.enabledDynamicColor){
            SwitchItem(
                title = stringResource(R.string.use_dynamic_colors),
                value = state.themeModel.useDynamicColor,
                onValueChange = {onAction(SettingsAction.SetDynamicTheme(it))}
            )
        }
    }
}
