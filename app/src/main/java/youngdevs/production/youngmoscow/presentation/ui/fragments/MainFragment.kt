package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.utilities.LoadingStatus
import youngdevs.production.youngmoscow.databinding.FragmentMainBinding
import youngdevs.production.youngmoscow.presentation.ui.adapter.EventsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.MainViewModel
import youngdevs.production.youngmoscow.presentation.viewmodel.SharedViewModel

// Фрагмент, отображающий список событий
@AndroidEntryPoint // аннотация для использования Hilt DI
class MainFragment : Fragment() {
    private var isBackPressed = false

    // ViewModel для работы с данными
    private val viewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    // Поле для привязки View Binding
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var eventsAdapter: EventsAdapter


    // Когда пользователь выбирает местоположение, вызываем setSelectedLocation() в SharedViewModel
    private fun onLocationSelected(location: String) {
        sharedViewModel.setSelectedLocation(location)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Инициализация View Binding и возвращение корневого View макета фрагмента
        _binding =
            FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем адаптер достопримечательностей
        eventsAdapter =
            EventsAdapter(viewLifecycleOwner.lifecycleScope)

        eventsAdapter.onItemClickListener = object : EventsAdapter.OnItemClickListener {
            override fun onItemClick(event: Event) {
                val action = MainFragmentDirections.actionMainFragmentToMapsFragment(event.address)
                findNavController().navigate(action)
            }
        }

        binding.searchField.addTextChangedListener { text ->
            viewModel.searchEvents(text.toString())
        }

        // Настраиваем RecyclerView с LinearLayoutManager и устанавливаем адаптер
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = eventsAdapter

        // Обрабатываем изменения данных в ViewModel и обновляем адаптер
        viewModel.events.observe(viewLifecycleOwner) { events
            ->
            eventsAdapter.submitList(events)
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
        viewModel.loadEvents()
    }

    fun handleOnBackPressed() {
        if (isBackPressed) {
            requireActivity().finish()
        } else {
            isBackPressed = true
            Toast.makeText(
                requireContext(),
                "Нажмите еще раз, чтобы выйти",
                Toast.LENGTH_SHORT
            )
                .show()
            Handler(Looper.getMainLooper())
                .postDelayed({ isBackPressed = false }, 2000)
        }
    }

    // Удаляем связывание с макетом при уничтожении представления
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
