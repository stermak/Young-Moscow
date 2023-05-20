package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentLanguageBinding
import youngdevs.production.youngmoscow.presentation.ui.activity.MainActivity
import youngdevs.production.youngmoscow.presentation.viewmodel.LanguageViewModel

@AndroidEntryPoint
class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LanguageViewModel by viewModels()

    private val languageNames: Array<String> by lazy {
        resources.getStringArray(R.array.language_names)
    }
    private val languageCodes: Array<String> by lazy {
        resources.getStringArray(R.array.language_codes)
    }
    private var selectedLanguagePosition: Int = -1
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val languageAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, languageNames)

        binding.languageListView.adapter = languageAdapter

        binding.languageListView.setOnItemClickListener { _, _, position, _ ->
            selectedLanguagePosition = position // Сохраняем позицию выбранного языка
            LanguageViewModel.changeLanguage(requireContext(), languageCodes[position])
            saveSelectedLanguage(languageCodes[position]) // Сохраняем выбранный язык в настройках
            restartActivity()
        }

        // При открытии фрагмента проверяем сохраненный язык и восстанавливаем позицию
        val savedLanguage = sharedPreferences.getString("SelectedLanguage", null)
        if (savedLanguage != null) {
            val savedLanguagePosition = languageCodes.indexOf(savedLanguage)
            if (savedLanguagePosition != -1) {
                selectedLanguagePosition = savedLanguagePosition
            }
        }
    }

    private fun saveSelectedLanguage(languageCode: String) {
        sharedPreferences.edit().putString("SelectedLanguage", languageCode).apply()
    }

    private fun restartActivity() {
        MainActivity.selectedLanguage = languageCodes[selectedLanguagePosition]
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
