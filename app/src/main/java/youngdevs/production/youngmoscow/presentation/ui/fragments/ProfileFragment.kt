package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import youngdevs.production.youngmoscow.data.repository.UserRepositoryImpl
import youngdevs.production.youngmoscow.databinding.FragmentProfileBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.ProfileViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var userRepository: UserRepositoryImpl
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Загрузите данные пользователя
        lifecycleScope.launch {
            loadUserData()

            binding.saveProfile.setOnClickListener {
                lifecycleScope.launch {
                    saveProfileChanges()
                }
            }

        }
    }


    private suspend fun loadUserData() {
        userRepository.getCurrentUser()?.let { userModel ->
            withContext(Dispatchers.Main) {
                binding.usernameEditTxt.setText(userModel.name)
                binding.emailEditTxt.setText(userModel.email)
                // Здесь вы можете добавить телефон и пароль пользователя, если они доступны в модели UserModel
            }
        }
    }

    private fun saveProfileChanges() {
        lifecycleScope.launch {
            val userId = userRepository.getCurrentUser()?.id ?: return@launch
            val name = binding.usernameEditTxt.text.toString()
            val email = binding.emailEditTxt.text.toString()
            val phone = binding.phoneEditTxt.text.toString()
            val password = binding.passwordEditTxt.text.toString()

            if (password.isNotEmpty()) {
                val dialog = ReauthenticateDialogFragment()
                dialog.setReauthenticateListener(object : ReauthenticateDialogFragment.ReauthenticateListener {
                    override fun onReauthenticate(currentPassword: String) {
                        reauthenticateUser(currentPassword) {
                            lifecycleScope.launch {
                                userRepository.updateUserPassword(password)
                            }
                        }
                    }
                })
                dialog.show(childFragmentManager, "ReauthenticateDialog")
            } else {
                saveUserData(name, email, phone, userId)
            }
        }
    }

    private fun saveUserData(name: String, email: String, phone: String, userId: String) {
        val userDocument = FirebaseFirestore.getInstance().collection("users").document(userId)

        // Создайте или обновите документ с указанными данными
        val userData = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone
        )

        userDocument.set(userData, SetOptions.merge())
            .addOnSuccessListener {
                // Обработка успешного создания или обновления документа
                Toast.makeText(requireContext(), "Профиль успешно обновлен", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Обработка ошибки
                Toast.makeText(requireContext(), "Ошибка при обновлении профиля", Toast.LENGTH_SHORT).show()
            }
    }

    private fun reauthenticateUser(currentPassword: String, onSuccess: () -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val credentials = EmailAuthProvider.getCredential(user?.email!!, currentPassword)

        user?.reauthenticate(credentials)
            ?.addOnSuccessListener {
                onSuccess()
                val name = binding.usernameEditTxt.text.toString()
                val email = binding.emailEditTxt.text.toString()
                val phone = binding.phoneEditTxt.text.toString()
                val userId = user.uid
                saveUserData(name, email, phone, userId)
            }
            ?.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Ошибка аутентификации: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

}