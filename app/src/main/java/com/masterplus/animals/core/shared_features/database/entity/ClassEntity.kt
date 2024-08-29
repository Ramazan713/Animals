package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Classes",
    foreignKeys = [
        ForeignKey(
            entity = PhylumEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("phylum_id"),
        )
    ]
)
data class ClassEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val scientific_name: String,
    val class_en: String,
    val class_tr: String,
    val phylum_id: Int,
    val image_path: String,
    val image_url: String,
    val created_at: String,
    val updated_at: String
)
