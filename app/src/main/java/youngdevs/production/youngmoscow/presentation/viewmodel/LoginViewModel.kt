package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase // зависимость от Use Case, который используется для аутентификации пользователя
) : ViewModel() {

    private val _isLoginSuccessful = MutableLiveData<Boolean?>() // LiveData для оповещения об успешной или неуспешной аутентификации
    val isLoginSuccessful: LiveData<Boolean?>
        get() = _isLoginSuccessful

    // Метод для аутентификации пользователя
    fun login(email: String, password: String) {
        viewModelScope.launch { // запуск корутины
            _isLoginSuccessful.value = authenticateUserUseCase.signIn(email, password) // вызов метода аутентификации пользователя и установка значения LiveData
        }
    }

    // Метод для обновления UI на основе текущего пользователя
    fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) { // если текущий пользователь не null, значит аутентификация прошла успешно
            _isLoginSuccessful.value = true // установка значения LiveData
        }
    }
}
