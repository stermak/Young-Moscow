package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.repository.KudaGoRepository
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val kudaGoRepository: KudaGoRepository, // зависимость от репозитория KudaGoRepository
    savedStateHandle: SavedStateHandle // handle для сохранения и получения состояния ViewModel
) : ViewModel() {
    val isInFavourite: MutableLiveData<Boolean> = MutableLiveData(false)

    private val eventId: Int = savedStateHandle.get<Int>("eventId") ?: 0 // получаем ID события из handle
    private val _event = MutableLiveData<Event>() // LiveData для получения информации о событии
    val event: LiveData<Event> get() = _event // LiveData для доступа к информации о событии



    init {
        viewModelScope.launch {
            _event.value = kudaGoRepository.getEventDetails(eventId) // получаем информацию о событии из репозитория
        }
    }
}