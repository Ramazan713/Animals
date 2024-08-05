package com.masterplus.animals.core.shared_features.savepoint.domain.enums

import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText

enum class SavePointDestinationType(
    override val title: UiText,
    val destinationTypeId: Int
): IMenuItemEnum {
    All(
        title = UiText.Text("Tümü"),
        destinationTypeId = 1
    ),
    ListType(
        title = UiText.Text("Liste"),
        destinationTypeId = 2
    ),
    Habitat(
        title = UiText.Text("Habitat"),
        destinationTypeId = 3
    ),
    ClassType(
        title = UiText.Text("Sınıf"),
        destinationTypeId = 4
    ),
    Order(
        title = UiText.Text("Takım"),
        destinationTypeId = 5
    ),
    Family(
        title = UiText.Text("Familya"),
        destinationTypeId = 6
    );

    override val iconInfo: IconInfo?
        get() = null

}