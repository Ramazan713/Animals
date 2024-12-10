package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import android.content.Context
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.utils.DateTimeFormatUtils
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toCategoryType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

class SavePointSuggestedTitleUseCase(
    private val categoryRepo: CategoryRepo,
    private val context: Context,
    private val translationRepo: TranslationRepo
) {

    suspend operator fun invoke(
        destinationTypeId: Int,
        destinationId: Int?,
        savePointContentType: SavePointContentType,
        kingdomType: KingdomType,
        saveMode: SavePointSaveMode
    ): SuggestedResult{
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val formattedDateTime = currentDateTime.format(DateTimeFormatUtils.dateTimeUntilMinuteFormat)

        val destinationTitle = getDestinationTitle(
            destinationId = destinationId,
            destinationTypeId = destinationTypeId,
            savePointContentType = savePointContentType,
            kingdomType = kingdomType
        ).asString(context)

        var title: String = ""
        if(saveMode.isAuto){
            title += "Auto - "
        }
        title += "$destinationTitle - $formattedDateTime"
        val uiTitle = UiText.Text(title)
        return SuggestedResult(
            title = uiTitle,
            titleText = title,
            currentDateTime = currentDateTime
        )
    }


    private suspend fun getDestinationTitle(
        destinationTypeId: Int,
        destinationId: Int?,
        savePointContentType: SavePointContentType,
        kingdomType: KingdomType
    ): UiText{

        val defaultResult =  SavePointDestination.All(kingdomType).title

        val destination = SavePointDestination.from(
            destinationTypeId = destinationTypeId,
            destinationId = destinationId,
            kingdomType = kingdomType
        )

        return when(savePointContentType){
            SavePointContentType.Category -> {
                destination?.title ?: defaultResult
            }
            SavePointContentType.Content -> {
                if(destinationId == null) return defaultResult
                val categoryType = destination?.toCategoryType() ?: return defaultResult

                return categoryRepo.getCategoryName(
                    categoryType = categoryType,
                    itemId = destinationId,
                    language = translationRepo.getLanguage()
                ) ?: defaultResult
            }
        }
    }


    companion object {
        data class SuggestedResult(
            val title: UiText,
            val titleText: String,
            val currentDateTime: LocalDateTime
        )
    }
}