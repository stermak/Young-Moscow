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

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events
    val loadingStatus = MutableLiveData<LoadingStatus>()

    private val _allEvents = mutableListOf<Event>()

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
            }
        }
    }

    fun searchEvents(query: String) {
        val filteredEvents = _allEvents.filter { it.name.contains(query, ignoreCase = true) }
        _events.value = filteredEvents
    }


    fun toggleFavorite(event: Event) {
        event.isFavorite = !event.isFavorite
        if (event.isFavorite) {
            addFavorite(event)
        } else {
            removeFavorite(event)
        }
    }

    private fun addFavorite(event: Event) {
        viewModelScope.launch {
            val favoriteEvent = FavoriteEvent(
                name = event.name,
                description = event.description,
                address = event.address,
                image = event.image,
                eventId = event.id
            )
            favoriteEventDao.addFavoriteEvent(favoriteEvent)
            Log.d("MainViewModel", "Added favorite event: ${favoriteEvent.name}")
        }
    }

    private fun removeFavorite(event: Event) {
        viewModelScope.launch {
            val favoriteEvent = favoriteEventDao.getFavoriteEventById(event.id)
            favoriteEvent?.let {
                favoriteEventDao.deleteFavoriteEvent(it)
                Log.d("MainViewModel", "Removed favorite event: ${it.name}")
            }
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
