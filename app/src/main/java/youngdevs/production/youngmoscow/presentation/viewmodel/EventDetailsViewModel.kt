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
    private val kudaGoRepository: KudaGoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val eventId: Int = savedStateHandle.get<Int>("eventId") ?: 0
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    init {
        viewModelScope.launch {
            _event.value = kudaGoRepository.getEventDetails(eventId)
        }
    }

    fun getFormattedDates(dates: List<Event.Date>): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val startDate = dates[0].start * 1000
        val endDate = dates[0].end * 1000
        return "С ${sdf.format(Date(startDate))} по ${sdf.format(Date(endDate))}"
    }

    fun getFormattedPlace(place: Event.Place): String {
        return "${place.title}, ${place.address}"
    }
}
