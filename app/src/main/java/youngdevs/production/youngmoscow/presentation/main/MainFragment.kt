package youngdevs.production.youngmoscow.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.app.YoungMoscow
import youngdevs.production.youngmoscow.data.adapters.EventsAdapter
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.presentation.event.EventDetailsFragment
import javax.inject.Inject

class MainFragment : Fragment(R.layout.fragment_main), EventsAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private val eventsAdapter = EventsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = eventsAdapter

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

    override fun onItemClick(event: Event) {
        val eventDetailsFragment = EventDetailsFragment.newInstance(event)
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, eventDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
}