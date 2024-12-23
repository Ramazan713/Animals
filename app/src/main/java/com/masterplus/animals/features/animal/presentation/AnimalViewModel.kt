
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.animal.domain.repo.DailyAnimalRepo
import com.masterplus.animals.features.animal.presentation.AnimalAction
import com.masterplus.animals.features.animal.presentation.AnimalState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimalViewModel(
    private val categoryRepo: CategoryRepo,
    private val savePointRepo: SavePointRepo,
    private val dailyAnimalRepo: DailyAnimalRepo,
    private val translationRepo: TranslationRepo
): ViewModel() {

    private val kingdomType = KingdomType.Animals
    private val _state = MutableStateFlow(AnimalState())
    val state = _state.asStateFlow()

    private val categoryTypes = listOf(
        CategoryType.Habitat,
        CategoryType.Order,
        CategoryType.Class,
        CategoryType.Family
    )

    init {
        loadCategories()
        loadSavePoints()
    }

    fun onAction(action: AnimalAction){
        when(action){
            AnimalAction.ClearMessage -> _state.update { it.copy(message = null) }
        }
    }

    private fun loadCategories(){
        translationRepo
            .getFlowLanguage()
            .onEach { language->
                _state.update { it.copy(
                    isLoading = true
                ) }
                viewModelScope.launch {
                    val results = categoryTypes.map { type ->
                        async {
                            type to categoryRepo
                                .getCategoryData(type, CATEGORY_LIMIT, language, kingdomType)
                        }
                    }

                    val dataMap = results.awaitAll().toMap()

                    val error = dataMap.values.firstNotNullOfOrNull { it.getFailureError?.text }

//                    val dailyAnimals = dailyAnimalRepo.getTodayAnimals(3, language = language)
//                    .mapNotNull { it.toImageWithTitleModel() }.let { imageWithTitleModels ->
//                        CategoryRowModel(imageWithTitleModels = imageWithTitleModels, showMore = false)
//                    }

                    _state.update { it.copy(
                        isLoading = false,
                        message = error,
                        habitats = dataMap[CategoryType.Habitat]?.getSuccessData?.let { imageWithTitleModels ->
                            CategoryDataRowModel(
                                categoryDataList = imageWithTitleModels,
                                showMore = imageWithTitleModels.size >= CATEGORY_LIMIT
                            )
                        } ?: CategoryDataRowModel(),

                        orders = dataMap[CategoryType.Order]?.getSuccessData?.let { imageWithTitleModels ->
                            CategoryDataRowModel(
                                categoryDataList = imageWithTitleModels,
                                showMore = imageWithTitleModels.size >= CATEGORY_LIMIT
                            )
                        } ?: CategoryDataRowModel(),

                        classes = dataMap[CategoryType.Class]?.getSuccessData?.let { imageWithTitleModels ->
                            CategoryDataRowModel(
                                categoryDataList = imageWithTitleModels,
                                showMore = imageWithTitleModels.size >= CATEGORY_LIMIT
                            )
                        } ?: CategoryDataRowModel(),

                        families = dataMap[CategoryType.Family]?.getSuccessData?.let { imageWithTitleModels ->
                            CategoryDataRowModel(
                                categoryDataList = imageWithTitleModels,
                                showMore = imageWithTitleModels.size >= CATEGORY_LIMIT
                            )
                        } ?: CategoryDataRowModel(),
//                    dailyAnimals = listOf()
                    ) }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadSavePoints(){
        savePointRepo
            .getAllSavePoints(
                contentType = SavePointContentType.Content,
                filteredDestinationTypeIds = null,
                kingdomType = KingdomType.Animals,
                filterBySaveMode = SavePointSaveMode.Manuel
            )
            .onEach { savePoints ->
                _state.update { it.copy(
                    savePoints = savePoints
                ) }
            }
            .launchIn(viewModelScope)
    }

    companion object {
        private const val CATEGORY_LIMIT = 7
    }
}