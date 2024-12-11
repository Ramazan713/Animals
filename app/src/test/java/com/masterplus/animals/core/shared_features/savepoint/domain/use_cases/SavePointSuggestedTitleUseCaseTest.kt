package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import com.masterplus.animals.core.data.repo.CategoryRepoFake
import com.masterplus.animals.core.data.repo.StringProviderFake
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toCategoryType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestinationType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.translation.data.repo.TranslationRepoFake
import com.masterplus.animals.test_utils.models.categoryData
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource


class SavePointSuggestedTitleUseCaseTest {
    private lateinit var useCase: SavePointSuggestedTitleUseCase
    private lateinit var categoryRepo: CategoryRepoFake
    private lateinit var stringProvider: StringProviderFake

    private val defaultKingdomType = KingdomType.Animals

    @BeforeEach
    fun setup(){
        categoryRepo = CategoryRepoFake()
        stringProvider = StringProviderFake()
    }

    @EnumSource(
        SavePointDestinationType::class,
        names = ["All"],
        mode = EnumSource.Mode.EXCLUDE
    )
    @ParameterizedTest
    fun whenDestinationIsNotAllAndItemIdIsNullAndContentTypeIsContent_shouldReturnDestinationTitle(
        destinationType: SavePointDestinationType
    ) = runTest {
        val contentType = SavePointContentType.Content
        val categoryName = "AllCategoryName"
        stringProvider.returnedValue = categoryName

        init()
        val response = execute(
            destinationTypeId = destinationType.destinationTypeId,
            savePointContentType = contentType,
            destinationId = null,
        )
        assertThat(response.titleText).contains(categoryName)
    }

    @EnumSource(
        SavePointDestinationType::class,
        names = ["All"],
        mode = EnumSource.Mode.EXCLUDE
    )
    @ParameterizedTest
    fun whenDestinationIsNotAllAndItemIdIsNullAndContentTypeIsCategory_shouldReturnDestinationTitle(
        destinationType: SavePointDestinationType
    ) = runTest {
        val contentType = SavePointContentType.Category
        val categoryName = "AllCategoryName"
        stringProvider.returnedValue = categoryName

        init()
        val response = execute(
            destinationTypeId = destinationType.destinationTypeId,
            savePointContentType = contentType,
            destinationId = null,
        )
        assertThat(response.titleText).contains(categoryName)
    }

    @EnumSource(
        SavePointDestinationType::class,
        names = ["All"],
        mode = EnumSource.Mode.EXCLUDE
    )
    @ParameterizedTest
    fun whenContentTypeIsCategoryAndDestinationIsNotAll_shouldReturnCategoryName(
        destinationType: SavePointDestinationType,
    ) = runTest {
        val itemId = 9
        val categoryName = "CategoryName"
        val contentType = SavePointContentType.Category
        val destination = SavePointDestination.from(
            destinationTypeId = destinationType.destinationTypeId,
            destinationId = itemId,
            kingdomType = defaultKingdomType
        )!!

        stringProvider.returnedValue = categoryName
        categoryRepo.fakeCategories.add(categoryData(
            id = itemId,
            title = categoryName,
            categoryType = destination.toCategoryType()!!
        ))

        init()

        val response = execute(
            destinationTypeId = destinationType.destinationTypeId,
            savePointContentType = contentType,
            destinationId = itemId,
        )
        assertThat(response.titleText).contains(categoryName)
    }

    @EnumSource(
        SavePointDestinationType::class,
        names = ["All"],
        mode = EnumSource.Mode.EXCLUDE
    )
    @ParameterizedTest
    fun whenContentTypeIsContentAndDestinationIsNotAll_shouldReturnCategoryName(
        destinationType: SavePointDestinationType,
    ) = runTest {
        val itemId = 9
        val categoryName = "CategoryName"
        val contentType = SavePointContentType.Content
        val destination = SavePointDestination.from(
            destinationTypeId = destinationType.destinationTypeId,
            destinationId = itemId,
            kingdomType = defaultKingdomType
        )!!

        stringProvider.returnedValue = categoryName
        categoryRepo.fakeCategories.add(categoryData(
            id = itemId,
            title = categoryName,
            categoryType = destination.toCategoryType()!!
        ))

        init()

        val response = execute(
            destinationTypeId = destinationType.destinationTypeId,
            savePointContentType = contentType,
            destinationId = itemId,
        )

        assertThat(response.titleText).contains(categoryName)
    }


    @EnumSource(SavePointContentType::class)
    @ParameterizedTest
    fun whenDestinationTypeIsAll_shouldTitleContainsTypeTitle(contentType: SavePointContentType) = runTest {
        init()
        val allCatTitle = "AllTitle"
        val destinationType = SavePointDestinationType.All

        stringProvider.returnedValue = allCatTitle
        val destinationTypeTitle = destinationType.title.asString(stringProvider)
        val response = execute(
            destinationTypeId = destinationType.destinationTypeId,
            savePointContentType = contentType
        )

        assertThat(response.titleText).contains(destinationTypeTitle)
    }

    @Test
    fun whenSaveModeAuto_shouldTitleContainsAuto() = runTest {
        init()
        val response = execute(saveMode = SavePointSaveMode.Auto)
        assertThat(response.titleText).contains("Auto")
    }

    @Test
    fun whenSaveModeManuel_shouldTitleDoesNotContainsAuto() = runTest {
        init()
        val response = execute(saveMode = SavePointSaveMode.Manuel)
        assertThat(response.titleText).doesNotContain("Auto")
    }

    private suspend fun execute(
        destinationTypeId: Int = SavePointDestinationType.All.destinationTypeId,
        destinationId: Int? = 1,
        savePointContentType: SavePointContentType = SavePointContentType.Content,
        kingdomType: KingdomType = defaultKingdomType,
        saveMode: SavePointSaveMode = SavePointSaveMode.Manuel,
    ): SavePointSuggestedTitleUseCase.Companion.SuggestedResult {
        return useCase.invoke(
            destinationTypeId, destinationId, savePointContentType, kingdomType, saveMode
        )
    }

    fun init(){
        useCase = SavePointSuggestedTitleUseCase(
            categoryRepo = categoryRepo,
            stringProvider = stringProvider,
            translationRepo = TranslationRepoFake()
        )
    }
}