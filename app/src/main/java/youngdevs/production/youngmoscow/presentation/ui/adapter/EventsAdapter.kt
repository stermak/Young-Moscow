package youngdevs.production.youngmoscow.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.databinding.ItemEventBinding

class EventsAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {
    private val events = mutableListOf<Event>()

    interface OnItemClickListener {
        fun onItemClick(event: Event)
    }
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
        val event = events[position]
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(event)
        }
    }

    override fun getItemCount() = events.size

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.eventTitle.text = event.formattedTitle
            binding.eventDescription.text = event.formattedDescription

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
        }
    }
}