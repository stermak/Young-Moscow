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


// Фрагмент, отображающий список избранных событий
@AndroidEntryPoint // аннотация для использования Hilt DI
class FavouriteEventsFragment : Fragment() {

    // Поле для привязки View Binding
    private var _binding: FragmentFavouriteEventsBinding? = null
    private val binding get() = _binding!!

    // ViewModel для работы с данными
    private val viewModel: FavouriteEventsViewModel by viewModels()

    // Адаптер для отображения списка событий
    private lateinit var favouriteEventsAdapter: FavouriteEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Инициализация View Binding
        _binding = FragmentFavouriteEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация адаптера и установка его в RecyclerView
        favouriteEventsAdapter = FavouriteEventsAdapter { eventId ->
            // Обработка нажатия на элемент события
            val action =
                FavouriteEventsFragmentDirections.actionFavouriteEventsFragmentToEventDetailsFragment(
                    eventId
                )
            findNavController().navigate(action)
        }
        binding.favouriteEventsRecyclerView.adapter = favouriteEventsAdapter

        // Наблюдение за изменением списка избранных событий и обновление адаптера
        viewModel.favouriteEvents.observe(viewLifecycleOwner) { events ->
            val reversedEvents =
                events.reversed() // Переворачиваем список, чтобы новые элементы добавлялись вверху
            favouriteEventsAdapter.submitList(reversedEvents)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Освобождение View Binding
        _binding = null
    }
}
