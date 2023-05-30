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
import youngdevs.production.youngmoscow.databinding.FragmentSettingsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.SettingsViewModel

// Аннотация AndroidEntryPoint говорит Hilt, чтобы инжектировать зависимости в этот фрагмент
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    // Инициализируем переменные
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private val settingsViewModel: SettingsViewModel by viewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    // Этот метод вызывается, когда Android создает макет для фрагмента.
    // Мы создаем макет из файла разметки и возвращаем его как результат.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                // Обновляем имя пользователя
                settingsViewModel.welcomeText()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Создаем макет из файла разметки и сохраняем ссылку на него в переменную binding.
        _binding =
            FragmentSettingsBinding.inflate(inflater, container, false)

        // Устанавливаем наблюдателей на объект settingsViewModel, чтобы получать уведомления об
        // изменениях в настройках.
        setObserver()

        // Устанавливаем обработчики событий на кнопки и другие элементы пользовательского
        // интерфейса.
        setEventListener()

        // Вызываем метод welcomeText() в settingsViewModel, чтобы получить приветственный текст.
        settingsViewModel.welcomeText()

        // Возвращаем макет как результат.
        return binding.root
    }

    // Метод устанавливает обработчики событий на кнопки и другие элементы пользовательского
    // интерфейса.
    private fun setEventListener() {
        binding.logout.setOnClickListener {
            // Вызываем метод exit() в settingsViewModel, чтобы выйти из приложения.
            settingsViewModel.exit()
            // Навигируем пользователя на экран входа в приложение.
            findNavController().navigate(R.id.loginFragment)
            // Скрываем нижнюю навигационную панель на экране входа в приложение.
            val bottomNavigation =
                activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigation?.visibility = View.GONE
        }

        binding.btnProfile.setOnClickListener {
            // Навигируем пользователя на экран избранных событий.
            findNavController()
                .navigate(R.id.action_settingsFragment_to_profileFragment)
        }
        binding.btnLanguage.setOnClickListener {
            // Навигируем пользователя на экран избранных событий.
            findNavController()
                .navigate(R.id.action_settingsFragment_to_languageFragment)
        }
        binding.btnSightseeings.setOnClickListener {
            // Навигируем пользователя на экран избранных событий.
            findNavController()
                .navigate(
                    R.id.action_settingsFragment_to_sightseeingsFragment
                )
        }
    }

    // Метод устанавливает наблюдателя на объект settingsViewModel, чтобы получать уведомления об
    // изменениях в настройках.
    private fun setObserver() {
        settingsViewModel.userName.observe(viewLifecycleOwner) { name ->
            // Обновляем текст приветствия на экране настроек.
            binding.welcomeText.text =
                getString(R.string.settings_welcome) + " " + name // здесь мы используем name как ник пользователя
        }
    }


    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }

    // Метод вызывается, когда фрагмент уничтожается.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
