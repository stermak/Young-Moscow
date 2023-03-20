package youngdevs.production.youngmoscow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    private val _registrationResult = MutableLiveData<Int?>()
    val registrationResult: LiveData<Int?>
        get() = _registrationResult

    fun registration(email: String, password: String, repeatPassword: String, name: String) {
        viewModelScope.launch {
            _registrationResult.value =
                authenticateUserUseCase.createAccount(email, password, repeatPassword, name)
        }
    }
}