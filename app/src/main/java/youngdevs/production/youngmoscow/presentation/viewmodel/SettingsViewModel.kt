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
    private val authenticateUserUseCase: AuthenticateUserUseCase // зависимость от Use Case, который используется для аутентификации пользователя
) : ViewModel() {

    private var _userName = MutableLiveData<String>() // LiveData для получения имени пользователя
    val userName: LiveData<String>
        get() = _userName

    // Метод для получения имени пользователя и установки его в LiveData
    fun welcomeText() {
        viewModelScope.launch { // запуск корутины
            authenticateUserUseCase.getCurrentUser()?.let { user -> // получаем текущего пользователя
                val name = user.name ?: "Unknown" // получаем имя пользователя или устанавливаем "Unknown", если имя не указано
                _userName.value = name // устанавливаем имя пользователя в LiveData
            }
        }
    }

    // Метод для выхода из аккаунта пользователя
    fun exit() {
        Firebase.auth.signOut() // выход из аккаунта в Firebase
        viewModelScope.launch { // запуск корутины
            authenticateUserUseCase.signOut() // вызов метода выхода из аккаунта в Use Case
        }
    }
}

