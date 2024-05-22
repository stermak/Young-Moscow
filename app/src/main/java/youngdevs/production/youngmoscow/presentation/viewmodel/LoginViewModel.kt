package youngdevs.production.youngmoscow.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import javax.inject.Inject

// LoginViewModel - аннотация Hilt, чтобы позволить DI фреймворку внедрять зависимости в этот класс
@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val userRepository: UserRepository
) : ViewModel() {
    private lateinit var googleSignInClient: GoogleSignInClient

    private val _isLoginSuccessful = MutableLiveData<Boolean?>()
    val isLoginSuccessful: LiveData<Boolean?> get() = _isLoginSuccessful

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

    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        if (!::googleSignInClient.isInitialized) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(activity, gso)
        }
        return googleSignInClient
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = authenticateUserUseCase.signInWithGoogle(idToken)
            if (result) {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                firebaseUser?.let { user ->
                    userRepository.createAccountWithGoogle(user.email ?: "", user.displayName ?: "")
                }
                _isLoginSuccessful.value = true
            } else {
                _isLoginSuccessful.value = false
            }
        }
    }
}

