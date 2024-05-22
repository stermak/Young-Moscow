package youngdevs.production.youngmoscow.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentProfileBinding
import youngdevs.production.youngmoscow.domain.models.UserModel
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.repository.UserRepositoryImpl
import youngdevs.production.youngmoscow.presentation.ui.fragments.ReauthenticateDialogFragment
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    fun uploadProfileImage(
        userRepository: UserRepositoryImpl,
        imageUri: Uri,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val downloadUrl = userRepository.uploadProfileImage(imageUri)
                userRepository.updateProfileImage(downloadUrl)
                Toast.makeText(context, "Фото профиля обновлено", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Ошибка при обновлении фото профиля: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    // Загрузить данные пользователя в UI
    fun loadUserData(
        userRepository: UserRepositoryImpl,
        binding: FragmentProfileBinding
    ) {
        viewModelScope.launch {
            userRepository.getCurrentUser()?.let { userModel: UserModel ->
                withContext(Dispatchers.Main) {
                    binding.usernameEditTxt.setText(userModel.name)
                    binding.emailEditTxt.setText(userModel.email)
                    binding.phoneEditTxt.setText(userModel.phone)
                    userModel.profileImage?.let { imageUrl: String ->
                        if (imageUrl.isNotEmpty()) {
                            Glide.with(binding.profilePic.context)
                                .load(imageUrl)
                                .centerCrop()
                                .placeholder(R.drawable.userpic)
                                .into(binding.profilePic)
                        }
                    }
                }
            }
        }
    }
            fun saveProfileChanges(
            userRepository: UserRepositoryImpl,
            binding: FragmentProfileBinding,
            fragmentManager: FragmentManager,
            context: Context
        ) {
            viewModelScope.launch {
                val userId = userRepository.getCurrentUser()?.id ?: return@launch
                val name = binding.usernameEditTxt.text.toString()
                val email = binding.emailEditTxt.text.toString()
                val phone = binding.phoneEditTxt.text.toString()
                val password = binding.passwordEditTxt.text.toString()

                if (password.isNotEmpty()) {
                    val signInMethods = userRepository.checkAccountExists(email)
                    if (signInMethods) {
                        // Если у пользователя есть метод входа по паролю, запускаем диалог повторной аутентификации
                        val currentPassword = binding.passwordEditTxt.text.toString()
                        if (currentPassword.isNotEmpty()) {
                            reauthenticateUser(currentPassword, userRepository, binding, context) {
                                viewModelScope.launch {
                                    userRepository.updateUserPassword(password)
                                    saveUserData(userRepository, name, email, phone, userId, context)
                                }
                            }
                        } else {
                            viewModelScope.launch {
                                userRepository.updateUserPassword(password)
                                saveUserData(userRepository, name, email, phone, userId, context)
                            }
                        }
                    } else {
                        // Если у пользователя нет метода входа по паролю, просто обновляем пароль без повторной аутентификации
                        viewModelScope.launch {
                            userRepository.updateUserPassword(password)
                            saveUserData(userRepository, name, email, phone, userId, context)
                        }
                    }
                } else {
                    saveUserData(userRepository, name, email, phone, userId, context)
                }
            }
        }

        private fun reauthenticateUser(
            currentPassword: String,
            userRepository: UserRepositoryImpl,
            binding: FragmentProfileBinding,
            context: Context,
            onReauthenticate: () -> Unit
        ) {
            viewModelScope.launch {
                try {
                    val email = userRepository.getCurrentUser()?.email ?: return@launch
                    val result = userRepository.reauthenticate(email, currentPassword)
                    if (result) {
                        onReauthenticate()
                    } else {
                        Toast.makeText(context, "Reauthentication failed", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Error reauthenticating", e)
                    Toast.makeText(context, "Reauthentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun saveUserData(
            userRepository: UserRepositoryImpl,
            name: String,
            email: String,
            phone: String,
            userId: String,
            context: Context
        ) {
            viewModelScope.launch {
                try {
                    userRepository.updateUserProfile(userId, name, email, phone)
                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Error updating profile", e)
                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
