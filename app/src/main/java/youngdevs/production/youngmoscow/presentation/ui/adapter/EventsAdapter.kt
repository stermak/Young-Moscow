package youngdevs.production.youngmoscow.presentation.ui.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.dao.FavoriteEventDao
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.entities.FavoriteEvent
import youngdevs.production.youngmoscow.data.services.RetrofitClient
import youngdevs.production.youngmoscow.databinding.ItemEventBinding

class EventsAdapter(
    private val scope: CoroutineScope,
    private val favoriteEventDao: FavoriteEventDao
) : ListAdapter<FavoriteEvent, EventsAdapter.EventViewHolder>(DiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(event: FavoriteEvent)
    }

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, scope, onItemClickListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent)
    }

    inner class EventViewHolder(
        private val binding: ItemEventBinding,
        private val scope: CoroutineScope,
        private val onItemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEvent: FavoriteEvent) {
            scope.launch {
                val event = favoriteEventDao.getEventById(favoriteEvent.eventId)
                event?.let {
                    binding.name.text = it.name
                    binding.description.text = it.description
                    binding.address.text = it.address
                    loadImage(it.image)
                }
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    onItemClickListener?.onItemClick(item)
                }
            }

            binding.favoriteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val favoriteEvent = getItem(position)
                    scope.launch {
                        val isFavorite = favoriteEventDao.getFavoriteEventById(favoriteEvent.eventId) != null
                        if (isFavorite) {
                            favoriteEventDao.deleteFavoriteEvent(favoriteEvent)
                        } else {
                            favoriteEventDao.addFavoriteEvent(favoriteEvent)
                        }
                    }
                }
            }
        }

        private fun loadImage(imageEventName: String) {
            val imagesEventsService = RetrofitClient.imagesEventsService
            scope.launch {
                try {
                    val response = imagesEventsService.getImageEvent(imageEventName.trim())
                    if (response.isSuccessful) {
                        val inputStream = response.body()?.byteStream()
                        inputStream?.let {
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            binding.image.setImageBitmap(bitmap)
                        }
                    } else {
                        Log.e("EventsAdapter", "Failed to load image: $imageEventName")
                    }
                } catch (e: Exception) {
                    Log.e("EventsAdapter", "Error loading image: $imageEventName", e)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<FavoriteEvent>() {
        override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
            return oldItem == newItem
        }
    }
}