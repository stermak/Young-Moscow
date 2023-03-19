package youngdevs.production.youngmoscow.presentation.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class MapsViewModelFactory @Inject constructor() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
