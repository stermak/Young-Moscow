package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.repository.KudaGoRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val kudaGoRepository: KudaGoRepository, // зависимость от репозитория KudaGoRepository
    savedStateHandle: SavedStateHandle // handle для сохранения и получения состояния ViewModel
) : ViewModel() {

    private val eventId: Int = savedStateHandle.get<Int>("eventId") ?: 0 // получаем ID события из handle
    private val _event = MutableLiveData<Event>() // LiveData для получения информации о событии
    val event: LiveData<Event> get() = _event // LiveData для доступа к информации о событии

    init {
        viewModelScope.launch { // запуск корутины
            _event.value = kudaGoRepository.getEventDetails(eventId) // получаем информацию о событии из репозитория
        }
    }

    // Форматирует даты события и возвращает строку
    fun getFormattedDates(dates: List<Event.Date>): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) // объект для форматирования даты
        val startDate = dates[0].start * 1000 // начальная дата в миллисекундах
        val endDate = dates[0].end * 1000 // конечная дата в миллисекундах
        return "С ${sdf.format(Date(startDate))} по ${sdf.format(Date(endDate))}" // форматируем даты и возвращаем строку
    }

    // Форматирует место проведения события и возвращает строку
    fun getFormattedPlace(place: Event.Place): String {
        return "${place.title}, ${place.address}" // возвращаем строку с названием места и адресом
    }
}
