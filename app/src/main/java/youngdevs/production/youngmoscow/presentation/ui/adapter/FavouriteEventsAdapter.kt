package youngdevs.production.youngmoscow.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.EventFavourite
import youngdevs.production.youngmoscow.databinding.ItemFavouriteEventBinding

class FavouriteEventsAdapter(private val onEventClick: (Int) -> Unit) :
    ListAdapter<EventFavourite, FavouriteEventsAdapter.FavouriteEventsViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteEventsViewHolder {
        val binding = ItemFavouriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteEventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteEventsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavouriteEventsViewHolder(private val binding: ItemFavouriteEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val eventId = getItem(position).eventId
                    onEventClick(eventId)
                }
            }
        }

        fun bind(eventFavourite: EventFavourite) {
            binding.favouriteEventTitle.text = eventFavourite.title
            binding.favouriteEventDescription.text = eventFavourite.description

            Glide.with(binding.root.context)
                .load(eventFavourite.imageUrl)
                .placeholder(R.drawable.photonet)
                .error(R.drawable.photonet)
                .into(binding.favouriteEventImage)
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<EventFavourite>() {
        override fun areItemsTheSame(oldItem: EventFavourite, newItem: EventFavourite): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: EventFavourite, newItem: EventFavourite): Boolean {
            return oldItem == newItem
        }
    }
}
