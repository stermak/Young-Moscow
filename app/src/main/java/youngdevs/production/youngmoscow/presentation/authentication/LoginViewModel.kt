package youngdevs.production.youngmoscow.presentation.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import javax.inject.Inject


class LoginViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    private val _isLoginSuccessful = MutableLiveData<Boolean?>()
    val isLoginSuccessful: LiveData<Boolean?>
        get() = _isLoginSuccessful

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoginSuccessful.value = authenticateUserUseCase.signIn(email, password)
        }
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            _isLoginSuccessful.value = true
        }
    }
}