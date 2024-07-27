package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "AnimalImages",
    foreignKeys = [
        ForeignKey(
            entity = AnimalEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("animal_id"),
        )
    ]
)
data class AnimalImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val animal_id: Int,
    val name: String?,
    val image_path: String,
    val image_url: String,
    val image_order: Int,
    val created_at: String,
    val updated_at: String
)
