package youngdevs.production.youngmoscow.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.android.gms.maps.model.LatLng as AndroidLatLng
import com.google.maps.model.LatLng as GmsLatLng

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

    fun Location.toGmsLatLng(): GmsLatLng {
        return GmsLatLng(latitude, longitude)
    }

    fun getLastKnownLocation(context: Context): Task<Location> {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw SecurityException("Location permission not granted")
        }
        return fusedLocationClient.lastLocation
    }

    fun getDirections(currentLocation: AndroidLatLng, destination: AndroidLatLng, apiKey: String): LiveData<DirectionsResult> {
        val directionsLiveData = MutableLiveData<DirectionsResult>()

        val geoApiContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()

        val origin = GmsLatLng(currentLocation.latitude, currentLocation.longitude)
        val dest = GmsLatLng(destination.latitude, destination.longitude)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = DirectionsApi.newRequest(geoApiContext)
                    .origin(origin)
                    .destination(dest)
                    .await()
                directionsLiveData.postValue(result)
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
        return directionsLiveData
    }

    fun updateCurrentLocation(location: Location) {
        _currentLocation.value = location
    }
}
