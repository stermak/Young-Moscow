package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            if (registrationResult != null) {
                if (registrationResult == 1) {
                    // Регистрация прошла успешно, но вместо перехода к следующему экрану,
                    // мы ждем успешного входа в систему с помощью AuthStateListener ниже
                }
            }
        }

        // Наблюдаем за состоянием аутентификации пользователя
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                // Пользователь успешно вошел в систему, переходим на следующий экран
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
            // Вызываем метод регистрации в viewModel с передачей данных из полей ввода.
            viewModel.registration(
                email = binding.emailField.text.toString().trim(' '),
                password =
                binding.passwordRegistrationField.text
                    .toString()
                    .trim(' '),
                repeatPassword =
                binding.repeatPasswordRegistrationField.text
                    .toString()
                    .trim(' '),
                name = binding.usernameField.text.toString().trim(' ')
            )
        }
    }

    // Метод вызывается, когда фрагмент уничтожается.
    // Очищаем viewModelStore от viewModel, чтобы предотвратить утечки памяти.
    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }
}
