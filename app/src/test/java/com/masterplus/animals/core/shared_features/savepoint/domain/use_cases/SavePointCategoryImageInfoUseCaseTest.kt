package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.masterplus.animals.core.data.repo.CategoryRepoFake
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestinationType
import com.masterplus.animals.core.shared_features.translation.data.repo.TranslationRepoFake
import com.masterplus.animals.test_utils.models.categoryData
import com.masterplus.animals.test_utils.models.classModel
import com.masterplus.animals.test_utils.models.familyModel
import com.masterplus.animals.test_utils.models.orderModel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class SavePointCategoryImageInfoUseCaseTest{
    private lateinit var useCase: SavePointCategoryImageInfoUseCase
    private lateinit var categoryRepo: CategoryRepoFake

    private val kingdomType = KingdomType.Animals
    private val imageData = "imageData"
    private val image = SampleDatas.imageWithMetadata


    @BeforeEach
    fun setUp() {
        categoryRepo = CategoryRepoFake()

        categoryRepo.fakeOrders.add(orderModel(
            image = image,
            kingdomType = kingdomType
        ))

        categoryRepo.fakeClasses.add(classModel(
            image = image,
            kingdomType = kingdomType
        ))

        categoryRepo.fakeFamilies.add(familyModel(
            image = image,
            kingdomType = kingdomType
        ))

        categoryRepo.fakeCategories.add(categoryData(
            image = image,
        ))

        useCase = SavePointCategoryImageInfoUseCase(
            categoryRepo = categoryRepo,
            translationRepo = TranslationRepoFake()
        )
    }

    @EnumSource(
        value = SavePointDestinationType::class,
        mode = EnumSource.Mode.INCLUDE,
        names = ["All", "ListType", "Habitat"]
    )
    @ParameterizedTest
    fun whenSavePointDestinationIsAllOrListTypeOrHabitat_shouldReturnNullableResult(
        destinationType: SavePointDestinationType
    ) = runTest {
        val destination = SavePointDestination.from(
            destinationTypeId = destinationType.destinationTypeId,
            destinationId = 1,
            kingdomType = kingdomType
        )

        val response = useCase.invoke(
            destination = destination!!,
        )

        assertThat(response.image).isNull()
    }

    @EnumSource(
        value = SavePointDestinationType::class,
        mode = EnumSource.Mode.INCLUDE,
        names = ["ClassType", "Family", "Order"]
    )
    @ParameterizedTest
    fun whenImageInfoExists_shouldReturn(
        destinationType: SavePointDestinationType
    ) = runTest{
        val destination = SavePointDestination.from(
            destinationTypeId = destinationType.destinationTypeId,
            destinationId = 1,
            kingdomType = kingdomType
        )

        val response = useCase.invoke(
            destination = destination!!,
        )

        assertThat(response.image).isEqualTo(image)
    }

}