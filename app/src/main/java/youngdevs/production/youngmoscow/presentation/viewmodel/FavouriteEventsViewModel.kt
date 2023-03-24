package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.EventFavourite
import youngdevs.production.youngmoscow.data.repository.FavouriteEventsRepository
import javax.inject.Inject

@HiltViewModel
class FavouriteEventsViewModel @Inject constructor(
    private val favouriteEventsRepository: FavouriteEventsRepository
) : ViewModel() {

    val favouriteEvents: LiveData<List<EventFavourite>> = favouriteEventsRepository.favouriteEvents

    fun addToFavourites(eventFavourite: EventFavourite) {
        viewModelScope.launch {
            favouriteEventsRepository.addToFavourites(eventFavourite)
        }
    }

    fun removeFromFavourites(eventId: Int) {
        viewModelScope.launch {
            favouriteEventsRepository.removeFromFavourites(eventId)
        }
    }

    suspend fun getEventById(eventId: Int): EventFavourite? {
        return favouriteEventsRepository.getEventById(eventId)
    }
}
