package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.repo.StringProvider
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
    private val stringProvider: StringProvider,
    private val translationRepo: TranslationRepo
) {

    suspend operator fun invoke(
        destinationTypeId: Int,
        destinationId: Int?,
        kingdomType: KingdomType,
        saveMode: SavePointSaveMode
    ): SuggestedResult{
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val formattedDateTime = currentDateTime.format(DateTimeFormatUtils.dateTimeUntilMinuteFormat)

        val destinationTitle = getDestinationTitle(
            destinationId = destinationId,
            destinationTypeId = destinationTypeId,
            kingdomType = kingdomType
        ).asString(stringProvider)

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
        kingdomType: KingdomType
    ): UiText{
        val defaultResult =  SavePointDestination.All(kingdomType).title

        val destination = SavePointDestination.from(
            destinationTypeId = destinationTypeId,
            destinationId = destinationId,
            kingdomType = kingdomType
        )
        val categoryType = destination?.toCategoryType()

        if(destinationId == null || categoryType == null)
            return destination?.title ?: defaultResult

        val catName = categoryRepo.getCategoryName(
            categoryType = categoryType,
            itemId = destinationId,
            language = translationRepo.getLanguage()
        )
        if(catName != null){
            return UiText.Text(catName)
        }
        return destination.title
    }


    companion object {
        data class SuggestedResult(
            val title: UiText,
            val titleText: String,
            val currentDateTime: LocalDateTime
        )
    }
}