package youngdevs.production.youngmoscow.presentation.ui.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.dao.FavoriteEventDao
import youngdevs.production.youngmoscow.data.entities.FavoriteEvent
import youngdevs.production.youngmoscow.data.services.RetrofitClient
import youngdevs.production.youngmoscow.databinding.ItemEventBinding
import youngdevs.production.youngmoscow.presentation.viewmodel.FavoriteEventsViewModel


class FavoriteEventsAdapter(
    private val scope: LifecycleCoroutineScope,
    private val favoriteEventDao: FavoriteEventDao,
    private val viewModel: FavoriteEventsViewModel

) : ListAdapter<FavoriteEvent, FavoriteEventsAdapter.FavoriteEventViewHolder>(DiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(event: FavoriteEvent)
    }

    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding, scope)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent)
    }



    inner class FavoriteEventViewHolder(
        private val binding: ItemEventBinding,
        private val scope: LifecycleCoroutineScope
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(favoriteEvent: FavoriteEvent) {
            binding.name.text = favoriteEvent.name
            binding.description.text = favoriteEvent.description
            binding.address.text = favoriteEvent.address
            binding.favoriteButton.setOnClickListener {
                viewModel.deleteFavoriteEvent(favoriteEvent)
            }
            loadImage(favoriteEvent.image)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener?.onItemClick(getItem(position))
            }
        }

        private fun loadImage(imageEventName: String) {
            val imagesEventsService = RetrofitClient.imagesEventsService
            scope.launch {
                try {
                    val response = imagesEventsService.getImageEvent(imageEventName.trim()) // Убедитесь, что нет пробелов перед именем файла
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
