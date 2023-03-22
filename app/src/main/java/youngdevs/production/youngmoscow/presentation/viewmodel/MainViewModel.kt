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
class MainViewModel @Inject constructor(private val repository: KudaGoRepository) : ViewModel() {

    private var loading = false
    private val _events = MutableLiveData<List<Event>>() // LiveData для получения списка событий
    val events: LiveData<List<Event>> get() = _events // LiveData для доступа к списку событий

    private var currentPage = 1

    init {
        fetchEvents() // Получаем список событий при создании ViewModel
    }
    // Метод для получения списка событий из репозитория
    private fun fetchEvents() {
        if (loading) {
            return
        }
        loading = true
        viewModelScope.launch {// запуск корутины
            val events = repository.getEvents( // получение списка событий из репозитория и установка его значения LiveData
                pageSize = 50,
                page = currentPage
            )
            _events.value = events
            loading = false
        }
    }

    fun isLoading(): Boolean {
        return loading
    }

    fun loadNextPage() {
        currentPage++
        fetchEvents()
    }
}
