package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.data.entities.FavoriteEvent
import youngdevs.production.youngmoscow.databinding.FragmentFavoriteEventsBinding
import youngdevs.production.youngmoscow.presentation.ui.activity.SpaceItemDecoration
import youngdevs.production.youngmoscow.presentation.ui.adapter.FavoriteEventsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.FavoriteEventsViewModel

@AndroidEntryPoint
class FavoriteEventsFragment : Fragment() {

    private var _binding: FragmentFavoriteEventsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteEventsViewModel by viewModels()
    private lateinit var favoriteEventsAdapter: FavoriteEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteEventsAdapter = FavoriteEventsAdapter(viewLifecycleOwner.lifecycleScope, viewModel.favoriteEventDao, viewModel)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = favoriteEventsAdapter

        val spaceDecoration = SpaceItemDecoration(16)
        binding.recyclerView.addItemDecoration(spaceDecoration)

        favoriteEventsAdapter.onItemClickListener = object : FavoriteEventsAdapter.OnItemClickListener {
            override fun onItemClick(event: FavoriteEvent) {
                // TODO: Handle the click event here
            }
        }

        viewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteEvents ->
            Log.d("FavoriteEventsFragment", "Favorite events updated: ${favoriteEvents.size}")
            favoriteEventsAdapter.submitList(favoriteEvents)
        }
        binding.deleteButton.setOnClickListener {
            viewModel.deleteAllFavoriteEvents()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
