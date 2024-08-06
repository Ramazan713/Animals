
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.presentation.mapper.toImageWithTitleModel
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.features.animal.domain.repo.DailyAnimalRepo
import com.masterplus.animals.features.animal.presentation.AnimalAction
import com.masterplus.animals.features.animal.presentation.AnimalState
import com.masterplus.animals.features.animal.presentation.models.CategoryRowModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimalViewModel(
    private val categoryRepo: CategoryRepo,
    private val savePointRepo: SavePointRepo,
    private val dailyAnimalRepo: DailyAnimalRepo
): ViewModel() {

    private val _state = MutableStateFlow(AnimalState())
    val state = _state.asStateFlow()

    init {
        loadCategories()
        loadSavePoints()
    }

    fun onAction(action: AnimalAction){

    }



    private fun loadCategories(){
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }
            val habitats = categoryRepo.getHabitatCategories(CATEGORY_LIMIT)
                .map { it.toImageWithTitleModel() }.let { imageWithTitleModels ->
                    CategoryRowModel(imageWithTitleModels = imageWithTitleModels, showMore = imageWithTitleModels.size >= CATEGORY_LIMIT)
                }
            val orders = categoryRepo.getOrders(CATEGORY_LIMIT)
                .map { it.toImageWithTitleModel() }.let { imageWithTitleModels ->
                    CategoryRowModel(imageWithTitleModels = imageWithTitleModels, showMore = imageWithTitleModels.size >= CATEGORY_LIMIT)
                }
            val classes = categoryRepo.getClasses(CATEGORY_LIMIT)
                .map { it.toImageWithTitleModel() }.let { imageWithTitleModels ->
                    CategoryRowModel(imageWithTitleModels = imageWithTitleModels, showMore = imageWithTitleModels.size >= CATEGORY_LIMIT)
                }
            val families = categoryRepo.getFamilies(CATEGORY_LIMIT)
                .map { it.toImageWithTitleModel() }.let { imageWithTitleModels ->
                    CategoryRowModel(imageWithTitleModels = imageWithTitleModels, showMore = imageWithTitleModels.size >= CATEGORY_LIMIT)
                }

            val dailyAnimals = dailyAnimalRepo.getTodayAnimals(3)
                .mapNotNull { it.toImageWithTitleModel() }.let { imageWithTitleModels ->
                    CategoryRowModel(imageWithTitleModels = imageWithTitleModels, showMore = false)
                }

            _state.update { it.copy(
                isLoading = false,
                habitats = habitats,
                orders = orders,
                classes = classes,
                families = families,
                dailyAnimals = dailyAnimals
            ) }
        }
    }

    private fun loadSavePoints(){
        val filter = SavePointDestination.All_DESTINATION_TYPE_IDS.filter { typeId -> typeId != SavePointDestination.ListType.DESTINATION_TYPE_ID }

        savePointRepo
            .getAllSavePointsByContentType(
                contentType = SavePointContentType.Content,
                filteredDestinationTypeIds = filter
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