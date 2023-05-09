package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.EventFavourite
import youngdevs.production.youngmoscow.data.repository.FavouriteEventsRepository
import javax.inject.Inject

// FavouriteEventsViewModel - класс ViewModel, который хранит и управляет данными избранных событий
@HiltViewModel
class FavouriteEventsViewModel
@Inject
constructor(
    // Внедрение зависимости для доступа к репозиторию избранных событий
    private val favouriteEventsRepository: FavouriteEventsRepository
) : ViewModel() {

    // Объявление LiveData для отслеживания списка избранных событий
    val favouriteEvents: LiveData<List<EventFavourite>> =
        favouriteEventsRepository.favouriteEvents

    // Функция для добавления события в избранное
    fun addToFavourites(eventFavourite: EventFavourite) {
        viewModelScope.launch {
            // Вызов метода репозитория для добавления события в избранное
            favouriteEventsRepository.addToFavourites(eventFavourite)
        }
    }

    // Функция для удаления события из избранного
    fun removeFromFavourites(eventId: Int) {
        viewModelScope.launch {
            // Вызов метода репозитория для удаления события из избранного
            favouriteEventsRepository.removeFromFavourites(eventId)
        }
    }

    // Функция для получения события по его идентификатору
    suspend fun getEventById(eventId: Int): EventFavourite? {
        // Вызов метода репозитория для получения события по его идентификатору
        return favouriteEventsRepository.getEventById(eventId)
    }
}
