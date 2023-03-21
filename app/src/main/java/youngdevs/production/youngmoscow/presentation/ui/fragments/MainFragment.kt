package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.databinding.FragmentMainBinding
import youngdevs.production.youngmoscow.presentation.ui.adapter.EventsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class MainFragment : Fragment(), EventsAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val eventsAdapter = EventsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root // возвращение корневого View макета фрагмента
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = eventsAdapter

        // Наблюдение за LiveData events в ViewModel и обновление списка событий в RecyclerView при изменении данных
        viewModel.events.observe(viewLifecycleOwner) { events ->
            eventsAdapter.setEvents(events)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // очистка переменной _binding для избежания утечек памяти
    }

    // Обработка нажатия на элемент списка
    override fun onItemClick(event: Event) {
        val eventId = event.id
        val navController = findNavController()
        navController.navigate(R.id.action_mainFragment_to_eventDetailsFragment, bundleOf("eventId" to eventId)) // переход к фрагменту EventDetailsFragment с передачей идентификатора события
    }
}

