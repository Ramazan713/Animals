
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.presentation.mapper.toImageWithTitleModel
import com.masterplus.animals.features.animal.domain.repo.AnimalCategoryRepo
import com.masterplus.animals.features.animal.presentation.AnimalAction
import com.masterplus.animals.features.animal.presentation.AnimalState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnimalViewModel(
    private val categoryRepo: AnimalCategoryRepo
): ViewModel() {

    private val _state = MutableStateFlow(AnimalState())
    val state = _state.asStateFlow()

    init {
        loadCategories()
    }

    fun onAction(action: AnimalAction){

    }



    private fun loadCategories(){
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }
            val habitats = categoryRepo.getHabitatCategories(CATEGORY_LIMIT).map { it.toImageWithTitleModel() }
            val orders = categoryRepo.getOrders(CATEGORY_LIMIT).map { it.toImageWithTitleModel() }
            val classes = categoryRepo.getClasses(CATEGORY_LIMIT).map { it.toImageWithTitleModel() }
            val families = categoryRepo.getFamilies(CATEGORY_LIMIT).map { it.toImageWithTitleModel() }

            _state.update { it.copy(
                isLoading = false,
                habitats = habitats,
                orders = orders,
                classes = classes,
                families = families
            ) }
        }
    }

    companion object {
        private const val CATEGORY_LIMIT = 7
    }
}