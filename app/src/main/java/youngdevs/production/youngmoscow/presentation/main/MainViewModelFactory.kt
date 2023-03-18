package youngdevs.production.youngmoscow.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import youngdevs.production.youngmoscow.data.repository.KudaGoRepository
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(private val repository: KudaGoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
