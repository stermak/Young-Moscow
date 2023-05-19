package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentRegistrationBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.RegistrationViewModel

// Аннотация AndroidEntryPoint говорит Hilt, чтобы инжектировать зависимости в этот фрагмент
@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    // Инициализируем переменные
    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModels()

    // Этот метод вызывается, когда Android создает макет для фрагмента.
    // Мы создаем макет из файла разметки и возвращаем его как результат.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Создаем макет из файла разметки и сохраняем ссылку на него в переменную binding.
        binding = FragmentRegistrationBinding.inflate(layoutInflater)

        // Устанавливаем наблюдателей на объект viewModel, чтобы получать уведомления об изменениях
        // в регистрации пользователя.
        setObserver()

        // Устанавливаем обработчики событий на кнопки и другие элементы пользовательского
        // интерфейса.
        setEventListener()

        // Возвращаем макет как результат.
        return binding.root
    }

    // Метод устанавливает наблюдателя на объект viewModel, чтобы получать уведомления об изменениях
    // в регистрации пользователя.
    private fun setObserver() {
        viewModel.registrationResult.observe(viewLifecycleOwner) { registrationResult ->
            when (registrationResult) {
                1 -> {
                    // Регистрация прошла успешно, перенаправляем пользователя на другой экран
                    findNavController().navigate(R.id.action_registrationFragment_to_navigation_main)
                }

                0 -> {
                    // Пользователь с таким email уже существует
                    Toast.makeText(
                        context,
                        "Пользователь с таким email уже существует",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                -1 -> {
                    // Пароли не совпадают
                    Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.accountExists.observe(viewLifecycleOwner) { exists ->
            if (exists) {
                Toast.makeText(
                    context,
                    "Пользователь с таким email уже существует",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                findNavController()
                    .navigate(
                        R.id
                            .action_registrationFragment_to_navigation_main
                    )

                val bottomNavigation =
                    activity?.findViewById<BottomNavigationView>(
                        R.id.nav_view
                    )
                bottomNavigation?.visibility = View.VISIBLE
            }
        }
    }


    // Метод устанавливает обработчики событий на кнопки и другие элементы пользовательского
    // интерфейса.
    private fun setEventListener() {
        binding.registration.setOnClickListener {
            val email = binding.emailField.text.toString().trim(' ')
            val password = binding.passwordRegistrationField.text.toString().trim(' ')
            val repeatPassword = binding.repeatPasswordRegistrationField.text.toString().trim(' ')
            val name = binding.usernameField.text.toString().trim(' ')
            viewModel.registration(email, password, repeatPassword, name)
        }
    }


    // Метод вызывается, когда фрагмент уничтожается.
    // Очищаем viewModelStore от viewModel, чтобы предотвратить утечки памяти.
    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }
}
