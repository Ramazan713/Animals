package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo

class SavePointCategoryImageInfoUseCase(
    private val translationRepo: TranslationRepo,
    private val categoryRepo: CategoryRepo
) {

    suspend operator fun invoke(destination: SavePointDestination): InfoResult{
        val nullableResult = InfoResult(null)
        val destinationId = destination.destinationId ?: return nullableResult

        val lang = translationRepo.getLanguage()

        when(destination){
            is SavePointDestination.All -> return nullableResult
            is SavePointDestination.ListType -> return nullableResult
            is SavePointDestination.Habitat -> {
                val habitat = categoryRepo.getHabitatWithId(destinationId, lang) ?: return nullableResult
                return InfoResult(habitat.image)
            }
            is SavePointDestination.ClassType -> {
                val classEntity = categoryRepo.getClassWithId(destinationId, lang) ?: return nullableResult
                return InfoResult(classEntity.image)
            }
            is SavePointDestination.Family -> {
                val family = categoryRepo.getFamilyWithId(destinationId, lang) ?: return nullableResult
                return InfoResult(family.image)
            }
            is SavePointDestination.Order -> {
                val order = categoryRepo.getOrderWithId(destinationId, lang) ?: return nullableResult
                return InfoResult(order.image)
            }
        }
    }

    companion object {
        data class InfoResult(
            val image: ImageWithMetadata?
        )
    }
}