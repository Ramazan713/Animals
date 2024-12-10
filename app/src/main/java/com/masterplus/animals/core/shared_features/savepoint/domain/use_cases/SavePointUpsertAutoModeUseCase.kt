package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo

class SavePointUpsertAutoModeUseCase(
    private val savePointRepo: SavePointRepo,
    private val suggestedTitleUseCase: SavePointSuggestedTitleUseCase
) {

    suspend operator fun invoke(
        destination: SavePointDestination,
        contentType: SavePointContentType,
        itemPosIndex: Int
    ){
        val saveMode = SavePointSaveMode.Auto
        val savePoint = savePointRepo.getSavePointByQuery(
            contentType = contentType,
            saveMode = saveMode,
            destination = destination
        )
        if(savePoint != null){
            savePointRepo.updateSavePointPos(savePoint.id ?: 0, itemPosIndex)
        }else{
            val title = suggestedTitleUseCase(
                destinationTypeId = destination.destinationTypeId,
                destinationId = destination.destinationId,
                savePointContentType = contentType,
                kingdomType = destination.kingdomType,
                saveMode = saveMode
            ).titleText
            when(contentType){
                SavePointContentType.Category -> {
                    savePointRepo.insertCategorySavePoint(
                        title = title,
                        destination = destination,
                        itemPosIndex = itemPosIndex,
                        saveMode = saveMode,
                    )
                }
                SavePointContentType.Content -> {
                    savePointRepo.insertContentSavePoint(
                        title = title,
                        destination = destination,
                        itemPosIndex = itemPosIndex,
                        saveMode = saveMode,
                    )
                }
            }
        }
    }
}