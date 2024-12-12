package com.masterplus.animals.features.settings.presentation.savepoint_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.features.settings.presentation.components.SettingSectionItem
import com.masterplus.animals.features.settings.presentation.components.SwitchItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SavePointSettingsPageRoot(
    viewModel: SavePointSettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    showCategorySection: Boolean,
    showSpeciesSection: Boolean
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SavePointSettingsPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        showCategorySection = showCategorySection,
        showSpeciesSection = showSpeciesSection
    )
}

private val HORIZONTAL_CONTENT_PADDING = PaddingValues(horizontal = 8.dp)
private val SWITCH_ITEM_CONTENT_PADDING = PaddingValues(horizontal = 8.dp , vertical = 4.dp)
private val SWITCH_ITEM_PADDING = PaddingValues(horizontal = 8.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavePointSettingsPage(
    state: SavePointSettingsState,
    onAction: (SavePointSettingsAction) -> Unit,
    onNavigateBack: () -> Unit,
    showCategorySection: Boolean,
    showSpeciesSection: Boolean
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Kayıt Noktası Ayarları") },
                navigationIcon = { NavigationBackIcon(onNavigateBack = onNavigateBack) },
                scrollBehavior = topAppBarScrollBehavior
            )
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(bottom = 24.dp, top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if(showCategorySection){
                item {
                    SettingSectionItem(
                        title = "Kategori Liste Sayfası",
                        titleContentPaddingValues = HORIZONTAL_CONTENT_PADDING,
                        modifier = Modifier.padding(SWITCH_ITEM_PADDING),
                        content = {
                            SwitchItem(
                                title = "Otomatik kayıt noktasını oluştur",
                                value = state.saveAutoSavePointForCategory,
                                subTitle = "Sayfa kapandığında son kalınan pozisyon için otomatik kayıt noktası oluşturur veya pozisyonu günceller",
                                onValueChange = {
                                    onAction(SavePointSettingsAction.ToggleCategorySaveSavePoint(it))
                                },
                                contentPaddingValues = SWITCH_ITEM_CONTENT_PADDING
                            )

                            SwitchItem(
                                title = "Otomatik kayıt noktasını yükle",
                                value = state.loadAutoSavePointForCategory,
                                subTitle = "Önceden kaydedilmiş otomatik kayıt noktası varsa, sayfa açıldığında kaydedilmiş pozisyonu yükler",
                                onValueChange = {
                                    onAction(SavePointSettingsAction.ToggleCategoryLoadSavePoint(it))
                                },
                                contentPaddingValues = SWITCH_ITEM_CONTENT_PADDING
                            )
                        }
                    )
                }
            }


            if(showSpeciesSection){
                item {
                    SettingSectionItem(
                        title = "Tür Liste Sayfası",
                        titleContentPaddingValues = HORIZONTAL_CONTENT_PADDING,
                        modifier = Modifier.padding(SWITCH_ITEM_PADDING),
                        content = {
                            SwitchItem(
                                title = "Otomatik kayıt noktasını oluştur",
                                value = state.saveAutoSavePointForSpecies,
                                subTitle = "Sayfa kapandığında son kalınan pozisyon için otomatik kayıt noktası oluşturur veya pozisyonu günceller",
                                onValueChange = {
                                    onAction(SavePointSettingsAction.ToggleSpeciesSaveSavePoint(it))
                                },
                                contentPaddingValues = SWITCH_ITEM_CONTENT_PADDING
                            )

                            SwitchItem(
                                title = "Otomatik kayıt noktasını yükle",
                                value = state.loadAutoSavePointForSpecies,
                                subTitle = "Önceden kaydedilmiş otomatik kayıt noktası varsa, sayfa açıldığında kaydedilmiş pozisyonu yükler",
                                onValueChange = {
                                    onAction(SavePointSettingsAction.ToggleSpeciesLoadSavePoint(it))
                                },
                                contentPaddingValues = SWITCH_ITEM_CONTENT_PADDING
                            )
                        }
                    )
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SavePointSettingsPreview(modifier: Modifier = Modifier) {
    SavePointSettingsPage(
        state = SavePointSettingsState(),
        onAction = {},
        onNavigateBack = {

        },
        showCategorySection = true,
        showSpeciesSection = true
    )
}