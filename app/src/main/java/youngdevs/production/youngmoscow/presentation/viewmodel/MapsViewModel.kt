package youngdevs.production.youngmoscow.presentation.viewmodel

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// MapsViewModel - класс ViewModel, который управляет разрешениями на доступ к геолокации
@HiltViewModel
class MapsViewModel @Inject constructor(
    // Внедрение зависимости для доступа к контексту приложения
    private val applicationContext: Application
) : ViewModel() {

    // Код запроса разрешения на доступ к геолокации
    private val locationRequestCode = 1000

    // Функция для проверки наличия разрешения на доступ к геолокации
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Функция для запроса разрешения на доступ к геолокации
    fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            locationRequestCode
        )
    }
}
