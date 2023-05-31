package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.EventFavourite
import youngdevs.production.youngmoscow.databinding.FragmentEventDetailsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.EventDetailsViewModel
import youngdevs.production.youngmoscow.presentation.viewmodel.FavouriteEventsViewModel

// EventDetailsFragment - аннотация AndroidEntryPoint, которая позволяет использовать DI фреймворк
// Hilt
@AndroidEntryPoint
class EventDetailsFragment : Fragment() {

    // создание ViewModel для фрагмента
    private val viewModel: EventDetailsViewModel by viewModels()

    // привязка макета фрагмента
    private var _binding: FragmentEventDetailsBinding? = null
    private val binding
        get() = _binding!!

    // создание ViewModel для работы с избранными событиями
    private val favouriteEventsViewModel: FavouriteEventsViewModel by
    viewModels()

    // получение аргументов фрагмента
    private val args: EventDetailsFragmentArgs by navArgs()

    // инфлейт макета фрагмента и возврат его View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId =
            args.eventId // получение ID события из аргументов фрагмента

        // наблюдение за LiveData в ViewModel, которая сообщает о том, находится ли событие в списке
        // избранных
        viewModel.isInFavourite.observe(viewLifecycleOwner) { isInFavourite
            ->
            updateFavouritesButtonText(isInFavourite)
        }

        // запуск корутины в lifecycleScope для получения избранного события из базы данных
        lifecycleScope.launch {
            val eventFavourite =
                favouriteEventsViewModel.getEventById(eventId)
            viewModel.isInFavourite.value = eventFavourite != null
        }

        // наблюдение за LiveData события в ViewModel
        viewModel.event.observe(viewLifecycleOwner) { event ->
            if (event != null) {
                binding.eventTitle.text =
                    event.formattedTitle // установка заголовка события
                binding.eventDescription.text =
                    event
                        .formattedDescription // установка описания события
                binding.eventBodyText.text =
                    event
                        .formattedBodyText // установка подробной информации о событии
                binding.eventPrice.text =
                    event.price // установка цены события
                binding.siteUrl.text =
                    event.site_url // установка ссылки на сайт

                // проверка на наличие изображения для события
                if (event.images.isNotEmpty()) {
                    val imageUrl =
                        event.images[0].image // получение URL изображения
                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.photonet)
                        .error(R.drawable.photonet)
                        .into(
                            binding.eventImage
                        ) // загрузка изображения с помощью Glide и установка его в ImageView
                } else {
                    binding.eventImage.setImageResource(
                        R.drawable.photonet
                    ) // установка изображения-заглушки, если изображения для события нет
                }
            }

            // обработка клика на кнопке добавления в избранное
            binding.addToFavouritesButton.setOnClickListener {
                val event = viewModel.event.value
                if (event != null) {
                    val eventId = event.id
                    val eventTitle = event.formattedTitle
                    val eventDescription = event.formattedDescription
                    val imageUrl =
                        if (event.images.isNotEmpty())
                            event.images[0].image
                        else ""

                    val eventFavourite =
                        EventFavourite(
                            eventId,
                            eventTitle,
                            eventDescription,
                            imageUrl
                        )

                    if (viewModel.isInFavourite.value == true) {
                        favouriteEventsViewModel.removeFromFavourites(
                            eventId
                        ) // удаление из избранного, если уже находится там
                        viewModel.isInFavourite.value = false
                    } else {
                        favouriteEventsViewModel.addToFavourites(
                            eventFavourite
                        ) // добавление в избранное, если еще не находится там
                        viewModel.isInFavourite.value = true
                    }
                }
            }
        }
    }

    // функция для обновления текста кнопки добавления в избранное в зависимости от того, является
    // ли событие избранным
    private fun updateFavouritesButtonText(isInFavourite: Boolean) {
        Log.d("EventDetailsFragment", "isInFavourite: $isInFavourite")
        binding.addToFavouritesButton.text =
            if (isInFavourite) "Удалить из избранного"
            else "Добавить в избранное"
    }

    // очистка binding для избежания утечек памяти
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}