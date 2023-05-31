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
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.entities.FavoriteEvent
import youngdevs.production.youngmoscow.data.services.EventsService
import youngdevs.production.youngmoscow.data.services.ImagesEventsService
import youngdevs.production.youngmoscow.data.utilities.LoadingStatus
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    // Внедряем сервисы для работы с достопримечательностями и изображениями
    private val eventsService: EventsService,
    private val imagesEventsService: ImagesEventsService
) : ViewModel() {

    // Используем MutableLiveData для изменения списка достопримечательностей внутри ViewModel
    private val _events = MutableLiveData<List<FavoriteEvent>>()

    // Объявляем LiveData для предоставления списка достопримечательностей во внешний код
    val events: LiveData<List<FavoriteEvent>> = _events
    val loadingStatus = MutableLiveData<LoadingStatus>()


    // Загружаем список достопримечательностей с использованием корутин
    private val _allEvents = mutableListOf<FavoriteEvent>()

    fun loadEvents() {
        loadingStatus.value = LoadingStatus.LOADING
        viewModelScope.launch {
            try {
                val events = eventsService.getEvents()
                _allEvents.clear()
                _allEvents.addAll(events)
                _events.value = events
                loadingStatus.value = LoadingStatus.LOADED
                Log.d(
                    "MainViewModel",
                    "Loaded ${events.size} events"
                )
            } catch (e: Exception) {
                loadingStatus.value = LoadingStatus.ERROR
                Log.e(
                    "MainViewModel",
                    "Failed to load events",
                    e
                )
                // Обработка ошибки
            }
        }
    }

    fun searchEvents(query: String) {
        val filteredEvents = _allEvents.filter { it.name.contains(query, ignoreCase = true) }
        _events.value = filteredEvents
    }

    // Загружаем изображение с использованием корутин и обрабатываем исключения
    suspend fun loadImage(imageEventName: String): Bitmap? {
        return try {
            val response = imagesEventsService.getImageEvent(imageEventName)
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