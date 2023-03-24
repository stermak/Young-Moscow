package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.databinding.FragmentFavouriteEventsBinding
import youngdevs.production.youngmoscow.presentation.ui.adapter.FavouriteEventsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.FavouriteEventsViewModel


@AndroidEntryPoint
class FavouriteEventsFragment : Fragment() {

    private var _binding: FragmentFavouriteEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteEventsViewModel by viewModels()

    private lateinit var favouriteEventsAdapter: FavouriteEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouriteEventsAdapter = FavouriteEventsAdapter { eventId ->
            // Обработка нажатия на элемент события
            val action = FavouriteEventsFragmentDirections.actionFavouriteEventsFragmentToEventDetailsFragment(eventId)
            findNavController().navigate(action)
        }
        binding.favouriteEventsRecyclerView.adapter = favouriteEventsAdapter


        viewModel.favouriteEvents.observe(viewLifecycleOwner) { events ->
            val reversedEvents = events.reversed()
            favouriteEventsAdapter.submitList(reversedEvents)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
