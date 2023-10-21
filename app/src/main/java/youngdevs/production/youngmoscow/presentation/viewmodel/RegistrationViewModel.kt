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
class RegistrationViewModel
@Inject
constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    private val _registrationResult = MutableLiveData<Int?>()
    val registrationResult: LiveData<Int?>
        get() = _registrationResult

    private val _accountExists = MutableLiveData<Boolean>()
    val accountExists: LiveData<Boolean>
        get() = _accountExists

    fun registration(
        email: String,
        password: String,
        repeatPassword: String,
        name: String
    ) {

        println("Starting registration with email: $email")

        if (password != repeatPassword) {
            _registrationResult.value = -1
            return
        }

        viewModelScope.launch {
            println("Inside coroutine, checking account existence for email: $email")
            val exists = authenticateUserUseCase.checkAccountExists(email)
            _accountExists.value = exists
            if (!exists) {
                println("Account doesn't exist. Trying to create...")
                _registrationResult.value =
                    authenticateUserUseCase.createAccount(
                        email,
                        password,
                        repeatPassword,
                        name
                    )
                println("Account creation result: ${_registrationResult.value}")
            } else {
                println("Account already exists.")
            }
        }
    }
}
