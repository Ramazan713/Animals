package com.masterplus.animals.features.bio_detail.presentation

import com.masterplus.animals.features.bio_detail.domain.enums.BioInfoPageEnum

sealed interface BioDetailAction {

    data class ChangePage(val page: BioInfoPageEnum): BioDetailAction
}