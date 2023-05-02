package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.databinding.FragmentMainBinding
import youngdevs.production.youngmoscow.presentation.ui.adapter.EventsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.MainViewModel

// Фрагмент, отображающий список событий
@AndroidEntryPoint // аннотация для использования Hilt DI
class MainFragment : Fragment(), EventsAdapter.OnItemClickListener {
    private var isBackPressed = false

    // ViewModel для работы с данными
    private val viewModel: MainViewModel by viewModels()

    // Поле для привязки View Binding
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    // Адаптер для отображения списка событий
    private val eventsAdapter = EventsAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инициализация View Binding и возвращение корневого View макета фрагмента
        _binding =
            FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка LayoutManager и адаптера в RecyclerView
        binding.recyclerView.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        binding.recyclerView.adapter = eventsAdapter

        // Наблюдение за LiveData events в ViewModel и обновление списка событий в RecyclerView при
        // изменении данных
        viewModel.events.observe(viewLifecycleOwner) { events ->
            eventsAdapter.setEvents(events)
        }

        // Загрузка следующей страницы при достижении конца списка
        binding.recyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager =
                        binding.recyclerView.layoutManager
                                as LinearLayoutManager
                    val lastVisiblePosition =
                        layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount
                    if (
                        lastVisiblePosition + 5 >= totalItemCount &&
                        !viewModel.isLoading()
                    ) {
                        viewModel.loadNextPage()
                    }
                }
            }
        )
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

    // Обработка нажатия на элемент списка
    override fun onItemClick(event: Event) {
        val eventId = event.id
        val navController = findNavController()
        // Переход к фрагменту EventDetailsFragment с передачей идентификатора события
        navController.navigate(
            R.id.action_mainFragment_to_eventDetailsFragment,
            bundleOf("eventId" to eventId)
        )
    }
}
