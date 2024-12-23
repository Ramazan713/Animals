package com.masterplus.animals.features.animal.presentation

sealed interface AnimalAction {

    data object ClearMessage: AnimalAction
}