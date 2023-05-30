package youngdevs.production.youngmoscow.presentation.ui.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.services.RetrofitClient
import youngdevs.production.youngmoscow.databinding.ItemEventBinding

class EventsAdapter(private val scope: LifecycleCoroutineScope) :
    ListAdapter<Event, EventsAdapter.EventViewHolder>(
        DiffCallback()
    ) {
    // Создание нового ViewHolder, которому передается экземпляр макета ItemSightseeingBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding =
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return EventViewHolder(binding, scope)
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int
    ) {
        val event = getItem(position)
        holder.bind(event)
    }

    // Определение класса SightseeingViewHolder, который наследуется от RecyclerView.ViewHolder
    class EventViewHolder(
        private val binding: ItemEventBinding,
        private val scope: LifecycleCoroutineScope
    ) : RecyclerView.ViewHolder(binding.root) {

        // Привязка данных к View
        fun bind(event: Event) {
            binding.name.text = event.name
            binding.description.text = event.description
            binding.address.text = event.address
            loadImage(event.image)
        }

        // Загрузка изображения с помощью ImagesService и отображение его в элементе списка
        private fun loadImage(imageEventName: String) {
            val imagesEventsService = RetrofitClient.imagesEventsService
            scope.launch {
                try {
                    val response =
                        imagesEventsService.getImageEvent(
                            imageEventName.trim()
                        ) // Убедитесь, что нет пробелов перед именем файла
                    if (response.isSuccessful) {
                        val inputStream = response.body()?.byteStream()
                        inputStream?.let {
                            val bitmap =
                                BitmapFactory.decodeStream(inputStream)
                            binding.image.setImageBitmap(bitmap)
                        }
                    } else {
                        Log.e(
                            "EventsAdapter",
                            "Failed to load image: $imageEventName"
                        )
                    }
                } catch (e: Exception) {
                    Log.e(
                        "EventsAdapter",
                        "Error loading image: $imageEventName",
                        e
                    )
                }
            }
        }
    }

    // Определение класса DiffCallback, который используется для оптимизации обновления списка
    // достопримечательностей
    private class DiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(
            oldItem: Event,
            newItem: Event
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Event,
            newItem: Event
        ): Boolean {
            return oldItem == newItem
        }
    }
}