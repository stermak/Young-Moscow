package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// MapsViewModel - класс ViewModel, который управляет разрешениями на доступ к геолокации
@HiltViewModel
class MapsViewModel @Inject constructor() : ViewModel() {

    val locationPermission: LiveData<Boolean> get() = _locationPermission
    private val _locationPermission = MutableLiveData<Boolean>()

    fun setLocationPermission(isGranted: Boolean) {
        _locationPermission.value = isGranted
    }
}
