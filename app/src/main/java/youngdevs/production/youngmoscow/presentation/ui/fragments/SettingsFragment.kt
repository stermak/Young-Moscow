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
import youngdevs.production.youngmoscow.databinding.FragmentSettingsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.SettingsViewModel

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setObserver()
        setEventListener()

        settingsViewModel.welcomeText()

        return binding.root
    }

    private fun setEventListener() {
        binding.logout.setOnClickListener {
            settingsViewModel.exit()
            findNavController().navigate(R.id.loginFragment)
            val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigation?.visibility = View.GONE
        }
    }

    private fun setObserver() {
        settingsViewModel.userName.observe(viewLifecycleOwner) {
            binding.welcomeText.text = getString(R.string.settings_welcome) + " " + it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}