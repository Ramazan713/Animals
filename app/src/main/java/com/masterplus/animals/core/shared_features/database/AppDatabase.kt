package com.masterplus.animals.core.shared_features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.masterplus.animals.core.shared_features.database.dao.AnimalDao
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.dao.ListAnimalsDao
import com.masterplus.animals.core.shared_features.database.dao.ListDao
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity.AnimalImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.GenusEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.ListAnimalsEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.view.ListViewEntity
import com.masterplus.trdictionary.core.data.local.services.ListViewDao

@Database(
    version = 1,
    entities = [
        AnimalEntity::class, AnimalImageEntity::class, ClassEntity::class, FamilyEntity::class,
        GenusEntity::class, HabitatCategoryEntity::class, OrderEntity::class, PhylumEntity::class,
        SpeciesEntity::class, ListEntity::class, ListAnimalsEntity::class
    ],
    views = [
        ListViewEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract val categoryDao: CategoryDao

    abstract val animalDao: AnimalDao

    abstract val listDao: ListDao

    abstract val listViewDao: ListViewDao

    abstract val listAnimalsDao: ListAnimalsDao

}