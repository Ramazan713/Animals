package com.masterplus.animals.features.bio_list.presentation

data class BioListState(
    val title: String = "Hayvanlar Listesi",
    val dialogEvent: BioListDialogEvent? = null,
    val listIdControl: Int? = null
)
