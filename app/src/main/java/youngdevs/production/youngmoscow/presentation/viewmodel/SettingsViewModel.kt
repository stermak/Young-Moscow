package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    private var _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    fun welcomeText() {
        viewModelScope.launch {
            authenticateUserUseCase.getCurrentUser()?.let { user ->
                val name = user.name ?: "Unknown"
                _userName.value = name
            }
        }
    }


    fun exit() {
        Firebase.auth.signOut()
        viewModelScope.launch {
            authenticateUserUseCase.signOut()
        }
    }
}
