package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.domain.models.UserModel
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

}

