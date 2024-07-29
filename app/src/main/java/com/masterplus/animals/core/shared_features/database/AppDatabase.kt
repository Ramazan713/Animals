package com.masterplus.animals.core.shared_features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.masterplus.animals.core.shared_features.database.dao.AnimalDao
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity.AnimalImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.GenusEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity

@Database(
    version = 1,
    entities = [
        AnimalEntity::class, AnimalImageEntity::class, ClassEntity::class, FamilyEntity::class,
        GenusEntity::class, HabitatCategoryEntity::class, OrderEntity::class, PhylumEntity::class,
        SpeciesEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract val categoryDao: CategoryDao

    abstract val animalDao: AnimalDao
}