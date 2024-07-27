
import androidx.lifecycle.ViewModel
import com.masterplus.animals.features.animal.presentation.AnimalAction
import com.masterplus.animals.features.animal.presentation.AnimalState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnimalViewModel(

): ViewModel() {

    private val _state = MutableStateFlow(AnimalState())
    val state = _state.asStateFlow()

    fun onAction(action: AnimalAction){

    }
}