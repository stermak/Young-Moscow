package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentRegistrationBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.RegistrationViewModel

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)

        setObserver()
        setEventListener()

        return binding.root
    }

    private fun setObserver() {
        viewModel.registrationResult.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it == 1) {
                    findNavController().navigate(R.id.action_registrationFragment_to_navigation_main)
                    val bottomNavigation =
                        activity?.findViewById<BottomNavigationView>(R.id.nav_view)
                    bottomNavigation?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setEventListener() {
        binding.registration.setOnClickListener {
            viewModel.registration(
                email = binding.emailField.text.toString().trim(' '),
                password = binding.passwordRegistrationField.text.toString().trim(' '),
                repeatPassword = binding.repeatPasswordRegistrationField.text.toString().trim(' '),
                name = binding.usernameField.text.toString().trim(' ')
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }

}