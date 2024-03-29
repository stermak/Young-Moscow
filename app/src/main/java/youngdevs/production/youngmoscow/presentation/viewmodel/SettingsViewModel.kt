package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import javax.inject.Inject

// SettingsViewModel - класс ViewModel, который управляет данными экрана настроек
@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    // Внедрение зависимости для доступа к use-case аутентификации пользователя
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    // Объявление MutableLiveData для отслеживания имени пользователя
    private var _userName = MutableLiveData<String?>()

    // Объявление LiveData для предоставления имени пользователя
    val userName: LiveData<String?>
        get() = _userName

    // Функция для получения приветственного текста на основе имени пользователя
    fun welcomeText() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            _userName.value = user?.name
        }
    }

    fun exit() {
        // Выход из аккаунта в Firebase Auth
        Firebase.auth.signOut()
    }
}
