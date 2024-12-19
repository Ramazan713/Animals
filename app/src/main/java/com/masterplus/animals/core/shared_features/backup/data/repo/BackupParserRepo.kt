package com.masterplus.animals.core.shared_features.backup.data.repo

import com.masterplus.animals.core.shared_features.backup.data.dtos.AllBackupData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class BackupParserRepo {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun toJson(backupData: AllBackupData): String? {
        return try {
            json.encodeToString(AllBackupData.serializer(),backupData)
        }catch (e: SerializationException){
            return null
        }
    }

    fun fromJson(data: String): AllBackupData? {
        return try {
            json.decodeFromString(AllBackupData.serializer(),data)
        }catch (e: IllegalArgumentException){
            return null
        }
    }
}