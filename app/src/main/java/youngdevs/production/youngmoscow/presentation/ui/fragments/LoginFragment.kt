package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentLoginBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.LoginViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val auth = Firebase.auth
        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        val currentUser = auth.currentUser
        viewModel.updateUI(currentUser)

        setObserver()
        setEventListener()

        return binding.root
    }

    private fun setObserver() {
        viewModel.isLoginSuccessful.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it == true) {
                    findNavController().navigate(R.id.action_loginFragment_to_navigation_main)
                    val bottomNavigation =
                        activity?.findViewById<BottomNavigationView>(R.id.nav_view)
                    bottomNavigation?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setEventListener() {
        binding.login.setOnClickListener {
            viewModel.login(binding.username.text.toString(), binding.password.text.toString())
        }

        binding.registrationButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }

}