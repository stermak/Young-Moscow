package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.data.utilities.LoadingStatus
import youngdevs.production.youngmoscow.databinding.FragmentSightseeingsBinding
import youngdevs.production.youngmoscow.presentation.ui.adapter.SightseeingsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.SightseeingsViewModel

// Используем AndroidEntryPoint для автоматического внедрения зависимостей с Hilt
@AndroidEntryPoint
class SightseeingsFragment : Fragment() {

    // Инициализируем ViewModel через делегат viewModels
    private val viewModel: SightseeingsViewModel by viewModels()

    // Используем nullable переменную для связывания и делегат для безопасного доступа к ней
    private var _binding: FragmentSightseeingsBinding? = null
    private val binding
        get() = _binding!!

    // Объявляем адаптер для списка достопримечательностей
    private lateinit var sightseeingsAdapter: SightseeingsAdapter

    // Создаем представление фрагмента, связываем его с макетом и возвращаем его
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentSightseeingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Когда представление создано, настраиваем адаптер, привязываем его к RecyclerView и
    // настраиваем ViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем адаптер достопримечательностей
        sightseeingsAdapter =
            SightseeingsAdapter(viewLifecycleOwner.lifecycleScope)

        // Настраиваем RecyclerView с LinearLayoutManager и устанавливаем адаптер
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = sightseeingsAdapter

        // Обрабатываем изменения данных в ViewModel и обновляем адаптер
        viewModel.sightseeings.observe(viewLifecycleOwner) { sightseeings
            ->
            sightseeingsAdapter.submitList(sightseeings)
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadingStatus.LOADING -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.standing.visibility = View.VISIBLE
                    binding.sitting.visibility = View.VISIBLE
                    binding.errorServer.visibility = View.VISIBLE
                    binding.sorry.visibility = View.VISIBLE
                    binding.imageView8.visibility = View.VISIBLE
                    binding.imageView9.visibility = View.VISIBLE
                }
                LoadingStatus.LOADED -> {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.standing.visibility = View.GONE
                    binding.sitting.visibility = View.GONE
                    binding.errorServer.visibility = View.GONE
                    binding.sorry.visibility = View.GONE
                    binding.imageView8.visibility = View.GONE
                    binding.imageView9.visibility = View.GONE
                }
                LoadingStatus.ERROR -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.standing.visibility = View.VISIBLE
                    binding.sitting.visibility = View.VISIBLE
                    binding.errorServer.visibility = View.VISIBLE
                    binding.sorry.visibility = View.VISIBLE
                    binding.imageView8.visibility = View.VISIBLE
                    binding.imageView9.visibility = View.VISIBLE
                }
            }
        }


        // Загружаем список достопримечательностей
        viewModel.loadSightseeings()
    }

    // Удаляем связывание с макетом при уничтожении представления
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
