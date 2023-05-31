package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.repository.KudaGoRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val repository: KudaGoRepository) : ViewModel() {

    private var loading = false
    private val _events = MutableLiveData<List<Event>>() // LiveData для получения списка событий
    val events: LiveData<List<Event>> get() = _events // LiveData для доступа к списку событий
    private var currentPage = 1

    private val _exhibitions = MutableLiveData<List<Event>>()
    val exhibitions: LiveData<List<Event>> get() = _exhibitions

    private val _partys = MutableLiveData<List<Event>>()
    val partys: LiveData<List<Event>> get() = _partys

    private val _holidays = MutableLiveData<List<Event>>()
    val holidays: LiveData<List<Event>> get() = _holidays

    init {
        fetchEvents()
        fetchExhibitions()
        fetchPartys()
        fetchHolidays()
    }

    fun isLoading(): Boolean {
        return loading
    }

    fun loadNextPage() {
        currentPage++
        fetchEvents()
        fetchExhibitions()
        fetchPartys()
        fetchHolidays()
    }

    private fun fetchEvents() {
        if (loading) {
            return
        }
        loading = true
        viewModelScope.launch {
            val events = repository.getEvents(pageSize = 50, page = currentPage)
            _events.value = events
            loading = false
        }
    }

    fun fetchExhibitions() {
        if (loading) {
            return
        }
        loading = true
        viewModelScope.launch {
            val exhibitions = repository.getExhibitions(pageSize = 50, page = currentPage)
            _exhibitions.value = exhibitions
            loading = false
        }
    }

    fun fetchPartys() {
        if (loading) {
            return
        }
        loading = true
        viewModelScope.launch {
            val partys = repository.getPartys(pageSize = 50, page = currentPage)
            _partys.value = partys
            loading = false
        }
    }

    fun fetchHolidays() {
        if (loading) {
            return
        }
        loading = true
        viewModelScope.launch {
            val holidays = repository.getHoliday(pageSize = 50, page = currentPage)
            _holidays.value = holidays
            loading = false
        }
    }
}