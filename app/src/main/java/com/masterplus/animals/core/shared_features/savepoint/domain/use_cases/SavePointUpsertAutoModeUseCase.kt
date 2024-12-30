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
        itemId: Int
    ){
        val saveMode = SavePointSaveMode.Auto
        val savePoint = savePointRepo.getSavePointByQuery(
            contentType = contentType,
            saveMode = saveMode,
            destination = destination
        )
        if(savePoint != null){
            savePointRepo.updateSavePointPos(savePoint.id ?: 0, itemId)
        }else{
            val title = suggestedTitleUseCase(
                destinationTypeId = destination.destinationTypeId,
                destinationId = destination.destinationId,
                kingdomType = destination.kingdomType,
                saveMode = saveMode
            ).titleText
            savePointRepo.insertSavePoint(
                title = title,
                destination = destination,
                itemId = itemId,
                saveMode = saveMode,
                contentType = contentType
            )
        }
    }
}