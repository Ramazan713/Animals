package com.masterplus.animals.core.shared_features.savepoint.domain.enums

enum class SavePointSaveMode(
    val modeId: Int
) {
    Manuel(
        modeId = 1
    ),
    Auto(
        modeId = 2
    );

    val isManuel get() = this == Manuel
    val isAuto get() = this == Auto

    companion object {
        val DEFAULT = Manuel

        fun fromModeId(modeId: Int): SavePointSaveMode {
            entries.forEach { mode ->
                if(mode.modeId == modeId) return mode
            }
            return DEFAULT
        }
    }
}