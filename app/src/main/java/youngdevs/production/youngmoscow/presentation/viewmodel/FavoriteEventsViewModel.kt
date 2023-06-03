package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.dao.FavoriteEventDao
import youngdevs.production.youngmoscow.data.entities.FavoriteEvent
import javax.inject.Inject

@HiltViewModel
class FavoriteEventsViewModel @Inject constructor(
    val favoriteEventDao: FavoriteEventDao
) : ViewModel() {
    val favoriteEvents: LiveData<List<FavoriteEvent>> = favoriteEventDao.getFavoriteEvents()


    fun deleteAllFavoriteEvents() {
        viewModelScope.launch {
            favoriteEventDao.deleteAllFavoriteEvents()
        }
    }
}
