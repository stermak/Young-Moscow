package youngdevs.production.youngmoscow.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.app.YoungMoscow
import youngdevs.production.youngmoscow.data.adapters.EventsAdapter
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.databinding.FragmentMainBinding
import javax.inject.Inject

class MainFragment : Fragment(), EventsAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private val eventsAdapter = EventsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = eventsAdapter

        viewModel.events.observe(viewLifecycleOwner) { events ->
            eventsAdapter.setEvents(events)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as YoungMoscow)
            .appComponent
            .inject(this)
    }

     override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(event: Event) {
        val eventId = event.id
        val navController = findNavController()
        navController.navigate(R.id.action_mainFragment_to_eventDetailsFragment, bundleOf("eventId" to eventId))
    }
}