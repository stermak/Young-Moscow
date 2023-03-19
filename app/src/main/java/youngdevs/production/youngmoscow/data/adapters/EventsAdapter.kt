package youngdevs.production.youngmoscow.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.databinding.ItemEventBinding

class EventsAdapter() : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {
    private val events = mutableListOf<Event>()

    fun setEvents(newEvents: List<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventTitle.text = event.title
            binding.eventDescription.text = event.description

            if (event.images.isNotEmpty()) {
                val imageUrl = event.images[0].image
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.photonet) // Замените на ваше изображение-заглушку
                    .error(R.drawable.photonet) // Замените на ваше изображение для ошибок
                    .into(binding.eventImage)
            } else {
                // Установите плейсхолдер, если нет изображения
                binding.eventImage.setImageResource(R.drawable.photonet) // Замените на ваше изображение-заглушку
            }
        }
    }
}
