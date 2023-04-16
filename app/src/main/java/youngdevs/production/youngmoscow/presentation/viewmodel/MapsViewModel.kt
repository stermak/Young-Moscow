package youngdevs.production.youngmoscow.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// MapsViewModel - класс ViewModel, который управляет разрешениями на доступ к геолокации
@HiltViewModel
class MapsViewModel @Inject constructor(
    // Внедрение зависимости для доступа к контексту приложения
    private val applicationContext: Application,
    private val googleMap: GoogleMap
) : ViewModel() {

}
