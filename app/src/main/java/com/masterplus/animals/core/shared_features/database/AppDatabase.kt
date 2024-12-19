package com.masterplus.animals.core.shared_features.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.masterplus.animals.core.shared_features.database.dao.AnimalDao
import com.masterplus.animals.core.shared_features.database.dao.BackupMetaDao
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.dao.HistoryDao
import com.masterplus.animals.core.shared_features.database.dao.ListSpeciesDao
import com.masterplus.animals.core.shared_features.database.dao.ListDao
import com.masterplus.animals.core.shared_features.database.dao.PlantDao
import com.masterplus.animals.core.shared_features.database.dao.SavePointDao
import com.masterplus.animals.core.shared_features.database.dao.SearchCategoryDao
import com.masterplus.animals.core.shared_features.database.dao.SearchSpeciesDao
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity.BackupMetaEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.GenusEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.HistoryEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageMetadataEntity
import com.masterplus.animals.core.shared_features.database.entity.KingdomEntity
import com.masterplus.animals.core.shared_features.database.entity.ListSpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity.PlantEntity
import com.masterplus.animals.core.shared_features.database.entity.SavePointEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesHabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.view.ListViewEntity
import com.masterplus.animals.core.shared_features.database.view.SpeciesRelationsView
import com.masterplus.trdictionary.core.data.local.services.ListViewDao

@Database(
    version = 1,
    entities = [
        KingdomEntity::class, AnimalEntity::class, SpeciesImageEntity::class, ClassEntity::class, FamilyEntity::class,
        GenusEntity::class, HabitatCategoryEntity::class, OrderEntity::class, PhylumEntity::class,
        SpeciesEntity::class, ListEntity::class, ListSpeciesEntity::class, ImageEntity::class, ImageMetadataEntity::class,
        SavePointEntity::class, HistoryEntity::class, PlantEntity::class, SpeciesHabitatCategoryEntity::class,
        BackupMetaEntity::class
    ],
    views = [
        ListViewEntity::class, SpeciesRelationsView::class
    ]
)
abstract class AppDatabase: RoomDatabase() {

    abstract val categoryDao: CategoryDao

    abstract val animalDao: AnimalDao

    abstract val plantDao: PlantDao

    abstract val listDao: ListDao

    abstract val listViewDao: ListViewDao

    abstract val listSpeciesDao: ListSpeciesDao

    abstract val savePointDao: SavePointDao

    abstract val speciesDao: SpeciesDao

    abstract val searchCategoryDao: SearchCategoryDao

    abstract val searchSpeciesDao: SearchSpeciesDao

    abstract val historyDao: HistoryDao

    abstract val backupMetaDao: BackupMetaDao
}