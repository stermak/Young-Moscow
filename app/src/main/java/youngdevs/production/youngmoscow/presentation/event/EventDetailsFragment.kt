package youngdevs.production.youngmoscow.presentation.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.databinding.FragmentEventDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsFragment : Fragment() {

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val event = requireArguments().getParcelable<Event>("event")
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

            if (event.dates.isNotEmpty()) {
                val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val startDate = event.dates[0].start * 1000
                val endDate = event.dates[0].end * 1000
                binding.eventDates.text = "С ${sdf.format(Date(startDate))} по ${sdf.format(Date(endDate))}"
            }

            event.place?.let { place ->
                binding.eventPlace.text = "${place.title}, ${place.address}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(event: Event): Fragment {
            val fragment = EventDetailsFragment()
            val args = Bundle()
            args.putParcelable("event", event)
            fragment.arguments = args
            return fragment
        }
    }
}
