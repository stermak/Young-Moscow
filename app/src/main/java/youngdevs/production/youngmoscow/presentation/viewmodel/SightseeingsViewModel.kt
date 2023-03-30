package youngdevs.production.youngmoscow.presentation.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.Sightseeing
import youngdevs.production.youngmoscow.data.services.ImagesService
import youngdevs.production.youngmoscow.data.services.SightseeingsService
import javax.inject.Inject

// Используем HiltViewModel для автоматического внедрения зависимостей с Hilt
@HiltViewModel
class SightseeingsViewModel @Inject constructor(
    // Внедряем сервисы для работы с достопримечательностями и изображениями
    private val sightseeingsService: SightseeingsService,
    private val imagesService: ImagesService
) : ViewModel() {

    // Используем MutableLiveData для изменения списка достопримечательностей внутри ViewModel
    private val _sightseeings = MutableLiveData<List<Sightseeing>>()
    // Объявляем LiveData для предоставления списка достопримечательностей во внешний код
    val sightseeings: LiveData<List<Sightseeing>> = _sightseeings

    // Загружаем список достопримечательностей с использованием корутин
    fun loadSightseeings() {
        viewModelScope.launch {
            try {
                val sightseeings = sightseeingsService.getSightseeings()
                _sightseeings.value = sightseeings
                Log.d("SightseeingsViewModel", "Loaded ${sightseeings.size} sightseeings")
            } catch (e: Exception) {
                Log.e("SightseeingsViewModel", "Failed to load sightseeings", e)
                // Обработка ошибки
            }
        }
    }

    // Загружаем изображение с использованием корутин и обрабатываем исключения
    suspend fun loadImage(imageName: String): Bitmap? {
        return try {
            val response = imagesService.getImage(imageName)
            if (response.isSuccessful) {
                val inputStream = response.body()?.byteStream()
                inputStream?.let { BitmapFactory.decodeStream(it) }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
