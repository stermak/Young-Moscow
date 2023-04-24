package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.data.repository.UserRepositoryImpl
import youngdevs.production.youngmoscow.databinding.FragmentProfileBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.ProfileViewModel
import javax.inject.Inject

// Используем AndroidEntryPoint для автоматического внедрения зависимостей с Hilt
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject // использование Dependency Injection для userRepository
    lateinit var userRepository: UserRepositoryImpl
    private lateinit var binding: FragmentProfileBinding

    // Инициализируем ViewModel через делегат activityViewModels
    private val viewModel: ProfileViewModel by viewModels()

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
        viewModel.loadUserData(userRepository, binding)

        // Обработчик нажатия на кнопку сохранения профиля
        binding.saveProfile.setOnClickListener {
            viewModel.saveProfileChanges(
                userRepository,
                binding,
                childFragmentManager,
                requireContext()
            )
        }
    }

}
