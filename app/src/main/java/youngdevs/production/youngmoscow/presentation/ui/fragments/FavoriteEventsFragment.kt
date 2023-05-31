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
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.dao.FavoriteEventDao
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.entities.FavoriteEvent
import youngdevs.production.youngmoscow.databinding.FragmentFavoriteEventsBinding
import youngdevs.production.youngmoscow.presentation.ui.adapter.EventsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.FavoriteEventsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteEventsFragment : Fragment() {

    private var _binding: FragmentFavoriteEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteEventsViewModel by viewModels()
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteEventDao: FavoriteEventDao = (requireActivity().application as App).database.favoriteEventDao()

        eventsAdapter = EventsAdapter(viewLifecycleOwner.lifecycleScope, favoriteEventDao)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = eventsAdapter

        eventsAdapter.onItemClickListener = object : EventsAdapter.OnItemClickListener {
            override fun onItemClick(event: FavoriteEvent) {
                // Handle item click event
            }
        }

        viewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            viewLifecycleOwner.lifecycleScope.launch {
                val eventList = favoriteEvents.mapNotNull { favoriteEvent ->
                    favoriteEventDao.getEventById(favoriteEvent.eventId)
                }
                eventsAdapter.submitList(eventList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}