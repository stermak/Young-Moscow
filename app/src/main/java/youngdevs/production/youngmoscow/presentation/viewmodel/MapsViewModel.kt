package youngdevs.production.youngmoscow.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// MapsViewModel - класс ViewModel, который управляет разрешениями на доступ к геолокации
@HiltViewModel
class MapsViewModel @Inject constructor(private val fusedLocationClient: FusedLocationProviderClient) : ViewModel() {
    private val _locationPermission = MutableLiveData<Boolean>()
    val locationPermission: LiveData<Boolean> get() = _locationPermission

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> get() = _currentLocation

    fun setLocationPermission(isGranted: Boolean) {
        _locationPermission.value = isGranted
    }

    fun getLastKnownLocation(context: Context): Task<Location> {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw SecurityException("Location permission not granted")
        }
        return fusedLocationClient.lastLocation
    }


    fun updateCurrentLocation(location: Location) {
        _currentLocation.value = location
    }
}

