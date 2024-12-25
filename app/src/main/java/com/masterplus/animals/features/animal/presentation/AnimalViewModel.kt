
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.domain.constants.K
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.domain.utils.asEmptyResult
import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
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
import org.intellij.lang.annotations.Language

class AnimalViewModel(
    private val categoryRepo: CategoryRepo,
    private val savePointRepo: SavePointRepo,
    private val dailyAnimalRepo: DailyAnimalRepo,
    private val translationRepo: TranslationRepo
): ViewModel() {

    private val kingdomType = KingdomType.Animals
    private val _state = MutableStateFlow(AnimalState())
    val state = _state.asStateFlow()

    init {
        loadCategories()
        loadSavePoints()
    }

    fun onAction(action: AnimalAction){
        when(action){
            AnimalAction.ClearMessage -> _state.update { it.copy(message = null) }
            is AnimalAction.RetryCategory -> {
                viewModelScope.launch {
                    when(action.categoryType){
                        CategoryType.Habitat -> loadHabits(state.value.languageEnum)
                        CategoryType.Class -> loadClasses(state.value.languageEnum)
                        CategoryType.Order -> loadOrders(state.value.languageEnum)
                        CategoryType.Family -> loadFamilies(state.value.languageEnum)
                        CategoryType.List -> null
                    }?.onFailure { error ->
                        _state.update { it.copy(
                            message = error.text
                        ) }
                    }
                }
            }
        }
    }

    private fun loadCategories(){
        translationRepo
            .getFlowLanguage()
            .onEach { language->
                _state.update { it.copy(
                    isLoading = false,
                    languageEnum = language
                ) }
                viewModelScope.launch {
                    val jobs = listOf(
                        async { loadHabits(language) },
                        async { loadClasses(language) },
                        async { loadOrders(language) },
                        async { loadFamilies(language) },
                    )
                    val jobsResponse = jobs.awaitAll()
                    val error = jobsResponse.firstNotNullOfOrNull { it.getFailureError?.text }
                    _state.update { it.copy(
                        message = error
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

    private suspend fun loadHabits(language: LanguageEnum): EmptyDefaultResult {
        _state.update { it.copy(
            habitats = CategoryDataRowModel(isLoading = true)
        ) }
        val result = loadCategoryData(CategoryType.Habitat, language)
        result.onSuccess {
            _state.update { state -> state.copy(
                habitats = CategoryDataRowModel(isLoading = false, categoryDataList = it, showMore = it.size >= CATEGORY_LIMIT)
            ) }
        }
        return result.asEmptyResult()
    }

    private suspend fun loadClasses(language: LanguageEnum): EmptyDefaultResult {
        _state.update { it.copy(
            classes = CategoryDataRowModel(isLoading = true)
        ) }
        val result = loadCategoryData(CategoryType.Class, language)
        result.onSuccess {
            _state.update { state -> state.copy(
                classes = CategoryDataRowModel(isLoading = false, categoryDataList = it, showMore = it.size >= CATEGORY_LIMIT)
            ) }
        }
        return result.asEmptyResult()
    }


    private suspend fun loadOrders(language: LanguageEnum): EmptyDefaultResult {
        _state.update { it.copy(
            orders = CategoryDataRowModel(isLoading = true)
        ) }
        val result = loadCategoryData(CategoryType.Order, language)
        result.onSuccess {
            _state.update { state -> state.copy(
                orders = CategoryDataRowModel(isLoading = false, categoryDataList = it, showMore = it.size >= CATEGORY_LIMIT)
            ) }
        }
        return result.asEmptyResult()
    }


    private suspend fun loadFamilies(language: LanguageEnum): EmptyDefaultResult {
        _state.update { it.copy(
            families = CategoryDataRowModel(isLoading = true)
        ) }
        val result = loadCategoryData(CategoryType.Family, language)
        result.onSuccess {
            _state.update { state -> state.copy(
                families = CategoryDataRowModel(isLoading = false, categoryDataList = it, showMore = it.size >= CATEGORY_LIMIT)
            ) }
        }
        return result.asEmptyResult()
    }

    private suspend fun loadCategoryData(
        type: CategoryType,
        language: LanguageEnum
    ): DefaultResult<List<CategoryData>> {
        return categoryRepo.getCategoryData(
            categoryType = type,
            limit = CATEGORY_LIMIT,
            language = language,
            kingdomType = kingdomType
        )
    }

    companion object {
        private const val CATEGORY_LIMIT = K.HOME_CATEGORY_PAGE_SIZE
    }
}