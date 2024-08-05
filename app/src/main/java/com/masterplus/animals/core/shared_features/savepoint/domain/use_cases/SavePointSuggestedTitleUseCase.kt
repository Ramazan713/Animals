package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import android.content.Context
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.utils.DateTimeFormatUtils
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toCategoryType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

class SavePointSuggestedTitleUseCase(
    private val categoryRepo: CategoryRepo,
    private val context: Context
) {

    suspend operator fun invoke(
        destinationTypeId: Int,
        destinationId: Int?,
        savePointContentType: SavePointContentType
    ): SuggestedResult{
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val formattedDateTime = currentDateTime.format(DateTimeFormatUtils.dateTimeUntilMinuteFormat)

        val destinationTitle = getDestinationTitle(
            destinationId = destinationId,
            destinationTypeId = destinationTypeId,
            savePointContentType = savePointContentType
        ).asString(context)

        val title = UiText.Text("$destinationTitle - $formattedDateTime")
        return SuggestedResult(
            title = title,
            currentDateTime = currentDateTime
        )
    }


    private suspend fun getDestinationTitle(
        destinationTypeId: Int,
        destinationId: Int?,
        savePointContentType: SavePointContentType
    ): UiText{

        val defaultResult = SavePointDestination.All.title

        val destination = SavePointDestination.from(
            destinationTypeId = destinationTypeId,
            destinationId = destinationId
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
                    itemId = destinationId
                ) ?: defaultResult
            }
        }
    }


    companion object {
        data class SuggestedResult(
            val title: UiText,
            val currentDateTime: LocalDateTime
        )
    }
}