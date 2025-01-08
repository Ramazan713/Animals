package com.masterplus.animals.features.search.domain.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Cloud
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class SearchType(
    override val title: UiText,
    override val iconInfo: IconInfo?
): IMenuItemEnum {
    Local(
        title = UiText.Text("Local"),
        iconInfo = IconInfo(imageVector = Icons.Default.Cached)
    ),
    Server(
        title = UiText.Text("Server"),
        iconInfo = IconInfo(imageVector = Icons.Default.Cloud)
    );

    val isLocal get() = this == Local
    val isServer get() = this == Server
}