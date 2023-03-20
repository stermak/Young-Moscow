package youngdevs.production.youngmoscow.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import youngdevs.production.youngmoscow.data.repository.KudaGoRepository
import javax.inject.Inject

class EventDetailsViewModelFactory @Inject constructor(
    private val kudaGoRepository: KudaGoRepository
) : ViewModelProvider.Factory {

    fun createWithEventId(eventId: Int): EventDetailsViewModel {
        return EventDetailsViewModel(kudaGoRepository, eventId)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        throw UnsupportedOperationException("Use 'createWithEventId' instead")
    }
}
