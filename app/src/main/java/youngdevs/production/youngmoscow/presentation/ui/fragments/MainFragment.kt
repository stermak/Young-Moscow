package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import youngdevs.production.youngmoscow.databinding.FragmentMainBinding
import youngdevs.production.youngmoscow.presentation.ui.adapter.EventsAdapter
import youngdevs.production.youngmoscow.presentation.viewmodel.MainViewModel

// Фрагмент, отображающий список событий
@AndroidEntryPoint // аннотация для использования Hilt DI
class MainFragment : Fragment() {
    private var isBackPressed = false

    // ViewModel для работы с данными
    private val viewModel: MainViewModel by viewModels()

    // Поле для привязки View Binding
    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var eventsAdapter: EventsAdapter

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

        eventsAdapter = EventsAdapter(viewLifecycleOwner.lifecycleScope, object : EventsAdapter.OnItemClickListener {
            override fun onItemClick(event: Event) {
                val action = MainFragmentDirections.actionMainFragmentToMapsFragment(event.address)
                findNavController().navigate(action)
            }
            override fun onFavoriteClick(event: Event) {
                viewModel.toggleFavorite(event)
            }
        })


        binding.searchField.addTextChangedListener { text ->
            viewModel.searchEvents(text.toString())
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = eventsAdapter

        viewModel.events.observe(viewLifecycleOwner) { events ->
            Log.d("MainFragment", "Events updated: ${events.size}")
            eventsAdapter.submitList(events)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}