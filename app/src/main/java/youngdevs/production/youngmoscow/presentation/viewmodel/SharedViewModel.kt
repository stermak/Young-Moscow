package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _selectedLocation = MutableLiveData<String>()
    val selectedLocation: LiveData<String> = _selectedLocation

    fun setSelectedLocation(location: String) {
        _selectedLocation.value = location
    }
}
