package youngdevs.production.youngmoscow.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import javax.inject.Inject

class AuthenticationViewModelFactory @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authenticateUserUseCase) as T
        }

        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(authenticateUserUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}