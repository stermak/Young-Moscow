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
import youngdevs.production.youngmoscow.data.dao.FavoriteEventDao
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
    private val eventsService: EventsService,
    private val imagesEventsService: ImagesEventsService,
    private val favoriteEventDao: FavoriteEventDao
) : ViewModel() {

    private val _eventToUpdate = MutableLiveData<Event>()
    val eventToUpdate: LiveData<Event> = _eventToUpdate
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events
    val loadingStatus = MutableLiveData<LoadingStatus>()

    private val _allEvents = mutableListOf<Event>()

    fun loadEvents() {
        loadingStatus.value = LoadingStatus.LOADING
        viewModelScope.launch {
            try {
                val events = eventsService.getEvents()

                for (event in events) {
                    val favoriteEvent = favoriteEventDao.getFavoriteEventById(event.id)
                    event.isFavorite = favoriteEvent != null
                }

                _allEvents.clear()
                _allEvents.addAll(events)
                _events.value = events
                loadingStatus.value = LoadingStatus.LOADED
                Log.d("MainViewModel", "Loaded ${events.size} events")
            } catch (e: Exception) {
                loadingStatus.value = LoadingStatus.ERROR
                Log.e("MainViewModel", "Failed to load events", e)
            }
        }
    }

    fun searchEvents(query: String) {
        val filteredEvents = _allEvents.filter { it.name.contains(query, ignoreCase = true) }
        _events.value = filteredEvents
    }


    fun toggleFavorite(event: Event) {
        if (event.isFavorite) {
            removeFavorite(event)
        } else {
            addFavorite(event)
        }
    }

    private fun addFavorite(event: Event) {
        viewModelScope.launch {
            val existingCount = favoriteEventDao.countEventWithId(event.id.toString())
            if (existingCount == 0) {
                val favoriteEvent = FavoriteEvent(
                    name = event.name,
                    description = event.description,
                    address = event.address,
                    image = event.image,
                    eventId = event.id
                )
                favoriteEventDao.addFavoriteEvent(favoriteEvent)
                event.isFavorite = true   // Установите статус избранного после добавления в БД
                Log.d("MainViewModel", "Added favorite event: ${favoriteEvent.name}")
            } else {
                Log.d("MainViewModel", "Event already exists in favorites: ${event.name}")
            }
            event.isFavorite = true
            _eventToUpdate.value = event
        }
    }

    private fun removeFavorite(event: Event) {
        viewModelScope.launch {
            val favoriteEvent = favoriteEventDao.getFavoriteEventById(event.id)
            favoriteEvent?.let {
                favoriteEventDao.deleteFavoriteEvent(it)
                event.isFavorite = false   // Сбросьте статус избранного после удаления из БД
                Log.d("MainViewModel", "Removed favorite event: ${it.name}")
            }
            event.isFavorite = false
            _eventToUpdate.value = event
        }
    }

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
