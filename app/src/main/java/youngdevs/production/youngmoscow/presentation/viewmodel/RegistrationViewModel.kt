package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import javax.inject.Inject

// RegistrationViewModel - класс ViewModel, который управляет регистрацией пользователя
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    // Внедрение зависимости для доступа к use-case аутентификации пользователя
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    // Объявление MutableLiveData для отслеживания результата регистрации
    private val _registrationResult = MutableLiveData<Int?>()

    // Объявление LiveData для предоставления результата регистрации
    val registrationResult: LiveData<Int?>
        get() = _registrationResult

    // Функция для регистрации пользователя с указанными данными
    fun registration(email: String, password: String, repeatPassword: String, name: String) {
        viewModelScope.launch {
            // Вызов метода use-case для создания аккаунта и установка результата в MutableLiveData
            _registrationResult.value =
                authenticateUserUseCase.createAccount(email, password, repeatPassword, name)
        }
    }
}
