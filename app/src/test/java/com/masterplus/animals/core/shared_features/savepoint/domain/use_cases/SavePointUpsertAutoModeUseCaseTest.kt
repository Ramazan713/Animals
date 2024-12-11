package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.masterplus.animals.core.data.repo.CategoryRepoFake
import com.masterplus.animals.core.data.repo.StringProviderFake
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.data.repo.FakeSavePointRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.translation.data.repo.TranslationRepoFake
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SavePointUpsertAutoModeUseCaseTest {

    private lateinit var useCase: SavePointUpsertAutoModeUseCase
    private lateinit var savePointRepo: FakeSavePointRepo
    private lateinit var suggestedTitleUseCase: SavePointSuggestedTitleUseCase


    @BeforeEach
    fun setUp() {
        savePointRepo = FakeSavePointRepo()
        val stringProvider = StringProviderFake()
        suggestedTitleUseCase = SavePointSuggestedTitleUseCase(
            categoryRepo = CategoryRepoFake(),
            stringProvider = stringProvider,
            translationRepo = TranslationRepoFake()
        )

        useCase = SavePointUpsertAutoModeUseCase(
            savePointRepo = savePointRepo,
            suggestedTitleUseCase = suggestedTitleUseCase
        )
    }

    @Test
    fun whenNoSavePointExists_shouldBeCreated() = runTest {
        val kingdomType = KingdomType.Animals
        val destination = SavePointDestination.All(kingdomType)
        val contentType = SavePointContentType.Content
        val itemPosIndex = 10

        useCase.invoke(
            destination = destination,
            itemPosIndex = itemPosIndex,
            contentType = SavePointContentType.Content
        )

        val returnedSavePoint = savePointRepo.getSavePointByQuery(
            destination = destination,
            contentType = contentType,
            saveMode = SavePointSaveMode.Auto
        )
        val suggestedTitle = suggestedTitleUseCase.invoke(
            destinationTypeId = destination.destinationTypeId,
            saveMode = SavePointSaveMode.Auto,
            kingdomType = kingdomType,
            destinationId = destination.destinationId
        ).titleText

        assertThat(returnedSavePoint).isNotNull()
        assertThat(returnedSavePoint!!.saveMode).isEqualTo(SavePointSaveMode.Auto)
        assertThat(returnedSavePoint.destination).isEqualTo(destination)
        assertThat(returnedSavePoint.contentType).isEqualTo(contentType)
        assertThat(returnedSavePoint.itemPosIndex).isEqualTo(itemPosIndex)
        assertThat(returnedSavePoint.title).isEqualTo(suggestedTitle)
    }

    @Test
    fun whenSavePointExists_shouldBePosUpdated() = runTest {
        val destination = SavePointDestination.All(KingdomType.Animals)
        val contentType = SavePointContentType.Content
        val itemPosIndex = 10

        savePointRepo.insertContentSavePoint(
            destination = destination,
            itemPosIndex = itemPosIndex,
            saveMode = SavePointSaveMode.Auto,
            title = "fake title"
        )

        val updatedItemPosIndex = 20

        useCase.invoke(
            destination = destination,
            itemPosIndex = updatedItemPosIndex,
            contentType = contentType
        )

        val returnedSavePoint = savePointRepo.getSavePointByQuery(
            destination = destination,
            contentType = contentType,
            saveMode = SavePointSaveMode.Auto
        )

        assertThat(returnedSavePoint).isNotNull()
        assertThat(returnedSavePoint!!.saveMode).isEqualTo(SavePointSaveMode.Auto)
        assertThat(returnedSavePoint.destination).isEqualTo(destination)
        assertThat(returnedSavePoint.contentType).isEqualTo(contentType)
        assertThat(returnedSavePoint.itemPosIndex).isEqualTo(updatedItemPosIndex)
    }

}