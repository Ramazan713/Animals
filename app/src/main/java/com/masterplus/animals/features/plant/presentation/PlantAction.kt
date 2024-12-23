package com.masterplus.animals.features.plant.presentation

sealed interface PlantAction {

    data object ClearMessage: PlantAction
}