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

    private val viewModel: EventDetailsViewModel by viewModels()
    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: EventDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = args.eventId

        viewModel.event.observe(viewLifecycleOwner) { event ->
            if (event != null) {
                binding.eventTitle.text = event.formattedTitle
                binding.eventDescription.text = event.formattedDescription
                binding.eventBodyText.text = event.formattedBodyText
                binding.eventPrice.text = event.price

                if (event.images.isNotEmpty()) {
                    val imageUrl = event.images[0].image
                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.photonet)
                        .error(R.drawable.photonet)
                        .into(binding.eventImage)
                } else {
                    binding.eventImage.setImageResource(R.drawable.photonet)
                }

                event.dates?.let { dates ->
                    if (dates.isNotEmpty()) {
                        binding.eventDates.text = viewModel.getFormattedDates(dates)
                    }
                }

                event.place?.let { place ->
                    binding.eventPlace.text = viewModel.getFormattedPlace(place)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
