package youngdevs.production.youngmoscow.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.databinding.FragmentEventDetailsBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.EventDetailsViewModel

@AndroidEntryPoint
class EventDetailsFragment : Fragment() {

    private val viewModel: EventDetailsViewModel by viewModels() // создание ViewModel для фрагмента
    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: EventDetailsFragmentArgs by navArgs() // получение аргументов фрагмента

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false) // инфлейт макета фрагмента
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = args.eventId // получение ID события из аргументов фрагмента

        viewModel.event.observe(viewLifecycleOwner) { event -> // наблюдение за LiveData события в ViewModel
            if (event != null) {
                binding.eventTitle.text = event.formattedTitle // установка заголовка события
                binding.eventDescription.text = event.formattedDescription // установка описания события
                binding.eventBodyText.text = event.formattedBodyText // установка подробной информации о событии
                binding.eventPrice.text = event.price // установка цены события

                if (event.images.isNotEmpty()) { // проверка на наличие изображения для события
                    val imageUrl = event.images[0].image // получение URL изображения
                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.photonet)
                        .error(R.drawable.photonet)
                        .into(binding.eventImage) // загрузка изображения с помощью Glide и установка его в ImageView
                } else {
                    binding.eventImage.setImageResource(R.drawable.photonet) // установка изображения-заглушки, если изображения для события нет
                }

                event.dates?.let { dates ->
                    if (dates.isNotEmpty()) {
                        binding.eventDates.text = viewModel.getFormattedDates(dates) // установка форматированных дат события
                    }
                }

                event.place?.let { place ->
                    binding.eventPlace.text = viewModel.getFormattedPlace(place) // установка форматированного места проведения события
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // очистка binding для избежания утечек памяти
    }
}