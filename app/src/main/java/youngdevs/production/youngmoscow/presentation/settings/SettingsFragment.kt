package youngdevs.production.youngmoscow.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.YoungMoscow.presentation.settings.SettingsViewModel
import com.example.YoungMoscow.presentation.settings.SettingsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.app.YoungMoscow
import youngdevs.production.youngmoscow.databinding.FragmentSettingsBinding
import javax.inject.Inject

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel : SettingsViewModel

    @Inject
    lateinit var settingsViewModelFactory : SettingsViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity?.applicationContext as YoungMoscow).appComponent.inject(this)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this,settingsViewModelFactory)[SettingsViewModel::class.java]
        setObserver()
        setEventListener()

        viewModel.welcomeText()

        return binding.root
    }

    private fun setEventListener(){
        binding.logout.setOnClickListener{
            viewModel.exit()
            findNavController().navigate(R.id.loginFragment)
            val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigation?.visibility = View.GONE
        }
    }

    private fun setObserver(){
        viewModel.userName.observe(viewLifecycleOwner){
            binding.welcomeText.text = getString(R.string.settings_welcome)+ " "+ it
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}