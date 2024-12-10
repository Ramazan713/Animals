
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.presentation.mapper.toImageWithTitleModel
import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.core.presentation.models.CategoryRowModel
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.plant.domain.repo.DailyPlantRepo
import com.masterplus.animals.features.plant.presentation.PlantAction
import com.masterplus.animals.features.plant.presentation.PlantState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class PlantViewModel(
    private val categoryRepo: CategoryRepo,
    private val savePointRepo: SavePointRepo,
    private val dailyPlantRepo: DailyPlantRepo,
    private val translationRepo: TranslationRepo
): ViewModel() {

    private val kingdomType = KingdomType.Plants
    private val _state = MutableStateFlow(PlantState())
    val state = _state.asStateFlow()

    init {
        loadCategories()
        loadSavePoints()
    }

    fun onAction(action: PlantAction){}

    private fun loadCategories(){
        translationRepo
            .getFlowLanguage()
            .onEach { language->
                _state.update { it.copy(
                    isLoading = true
                ) }
                val habitats = categoryRepo.getCategoryData(CategoryType.Habitat, CATEGORY_LIMIT, language, kingdomType).let { imageWithTitleModels ->
                        CategoryDataRowModel(categoryDataList = imageWithTitleModels, showMore = imageWithTitleModels.size >= CATEGORY_LIMIT)
                    }
                val orders = categoryRepo.getCategoryData(CategoryType.Order, CATEGORY_LIMIT, language, kingdomType).let { imageWithTitleModels ->
                        CategoryDataRowModel(categoryDataList = imageWithTitleModels, showMore = imageWithTitleModels.size >= CATEGORY_LIMIT)
                    }
                val classes = categoryRepo.getCategoryData(CategoryType.Class, CATEGORY_LIMIT, language, kingdomType).let { imageWithTitleModels ->
                        CategoryDataRowModel(categoryDataList = imageWithTitleModels, showMore = imageWithTitleModels.size >= CATEGORY_LIMIT)
                    }
                val families = categoryRepo.getCategoryData(CategoryType.Family, CATEGORY_LIMIT, language, kingdomType).let { imageWithTitleModels ->
                        CategoryDataRowModel(categoryDataList = imageWithTitleModels, showMore = imageWithTitleModels.size >= CATEGORY_LIMIT)
                    }

                val dailyPlants = dailyPlantRepo.getTodayPlants(3, language = language)
                    .mapNotNull { it.toImageWithTitleModel() }.let { imageWithTitleModels ->
                        CategoryRowModel(imageWithTitleModels = imageWithTitleModels, showMore = false)
                    }

                _state.update { it.copy(
                    isLoading = false,
                    habitats = habitats,
                    orders = orders,
                    classes = classes,
                    families = families,
                    dailyPlants = dailyPlants
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadSavePoints(){
        savePointRepo
            .getAllSavePoints(
                contentType = SavePointContentType.Content,
                filteredDestinationTypeIds = null,
                kingdomType = KingdomType.Plants,
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