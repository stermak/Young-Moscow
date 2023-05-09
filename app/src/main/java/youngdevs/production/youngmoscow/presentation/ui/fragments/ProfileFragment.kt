package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.domain.repository.UserRepositoryImpl
import youngdevs.production.youngmoscow.databinding.FragmentProfileBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.ProfileViewModel
import javax.inject.Inject

// Используем AndroidEntryPoint для автоматического внедрения зависимостей с Hilt
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    companion object {
        private const val SELECT_IMAGE_REQUEST_CODE = 100
    }

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
        viewModel.loadUserData(userRepository, binding)
        binding.saveProfile.setOnClickListener {
            viewModel.saveProfileChanges(
                userRepository,
                binding,
                childFragmentManager,
                requireContext()
            )
        }
        binding.uploadPhotoButton.setOnClickListener {
            selectImageFromGallery()
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            selectedImage?.let {
                binding.profilePic.setImageURI(selectedImage)
                viewModel.uploadProfileImage(userRepository, it, requireContext())
            }
        }
    }
}
