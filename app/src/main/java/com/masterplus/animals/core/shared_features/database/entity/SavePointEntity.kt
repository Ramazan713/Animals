package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode

@Entity(
    tableName = "SavePoints",
    foreignKeys = [
        ForeignKey(
            entity = KingdomEntity::class,
            parentColumns = ["id"],
            childColumns = ["kingdomId"]
        ),
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("imageId"),
        )
    ]
)
data class SavePointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val titleEn: String,
    val titleTr: String,
    val contentTypeId: Int,
    val destinationTypeId: Int,
    val destinationId: Int?,
    val kingdomId: Int,
    val saveModeId: Int = SavePointSaveMode.DEFAULT.modeId,
    val saveKey: String?,
    val itemPosIndex: Int,
    val modifiedTime: String,
    val imageId: Int?,
)
