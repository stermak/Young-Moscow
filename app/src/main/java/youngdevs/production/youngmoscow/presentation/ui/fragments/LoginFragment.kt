package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentLoginBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.LoginViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {

    companion object {
        private const val RC_SIGN_IN = 9001 // Код для идентификации GoogleSignInActivity
    }


    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels() // Инициализация ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val auth = Firebase.auth // Инициализация Firebase Authentication
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        // Проверка, авторизован ли текущий пользователь и обновление UI в зависимости от результата проверки
        val currentUser = auth.currentUser
        viewModel.updateUI(currentUser)

        setObserver() // Установка Observer на LiveData isLoginSuccessful в ViewModel
        setEventListener() // Установка обработчиков событий для кнопок

        return binding.root // Возвращение корневого View макета фрагмента
    }

    // Наблюдение за LiveData isLoginSuccessful в ViewModel
    private fun setObserver() {
        viewModel.isLoginSuccessful.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it == true) {
                    // Если пользователь успешно авторизован, переход к главному экрану приложения и отображение BottomNavigationView
                    findNavController().navigate(R.id.action_loginFragment_to_navigation_main)
                    val bottomNavigation =
                        activity?.findViewById<BottomNavigationView>(R.id.nav_view)
                    bottomNavigation?.visibility = View.VISIBLE
                }
            }
        }
    }

    // Установка обработчиков событий для кнопок
    private fun setEventListener() {
        binding.login.setOnClickListener {
            viewModel.login(
                binding.username.text.toString(),
                binding.password.text.toString()
            ) // Попытка авторизации с помощью ViewModel
        }

        binding.registrationButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment) // Переход к экрану регистрации при нажатии кнопки "Зарегистрироваться"
        }
        binding.googleLogin.setOnClickListener {
            signInWithGoogle() // Авторизация через Google
        }
    }


    private fun signInWithGoogle() {
        val signInIntent =
            viewModel.getGoogleSignInClient(requireActivity()).signInIntent // Получение GoogleSignInClient и вызов активности для авторизации через Google
        startActivityForResult(
            signInIntent,
            RC_SIGN_IN
        ) // Запуск активности и передача кода для идентификации
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) { // Проверка кода для идентификации
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data) // Получение результата авторизации через Google
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.firebaseAuthWithGoogle(account.idToken!!) // Авторизация через Firebase с помощью полученного idToken
            } catch (e: ApiException) {
                Log.e(
                    "LoginFragment",
                    "signInResult:failed code=" + e.statusCode + " message=" + e.message
                ) // Вывод сообщения об ошибке в случае неудачной авторизации
                Toast.makeText(requireContext(), "Ошибка входа через Google", Toast.LENGTH_SHORT)
                    .show() // Показ сообщения об ошибке в случае неудачной авторизации
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear() // Очистка ViewModelStore для избежания утечек памяти
    }
}