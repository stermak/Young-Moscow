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

// FavouriteEventsAdapter - адаптер для отображения списка избранных событий в RecyclerView
class FavouriteEventsAdapter(private val onEventClick: (Int) -> Unit) :
    ListAdapter<
            EventFavourite, FavouriteEventsAdapter.FavouriteEventsViewHolder
            >(EventDiffCallback()) {

    // Создание ViewHolder для элементов списка избранных событий
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteEventsViewHolder {
        val binding =
            ItemFavouriteEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return FavouriteEventsViewHolder(binding)
    }

    // Привязка данных избранного события к ViewHolder
    override fun onBindViewHolder(
        holder: FavouriteEventsViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    // ViewHolder для элемента списка избранных событий
    inner class FavouriteEventsViewHolder(
        private val binding: ItemFavouriteEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // Обработка клика на элементе списка
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val eventId = getItem(position).eventId
                    onEventClick(eventId)
                }
            }
        }

        // Привязка данных избранного события к элементам разметки
        fun bind(eventFavourite: EventFavourite) {
            binding.favouriteEventTitle.text = eventFavourite.title
            binding.favouriteEventDescription.text =
                eventFavourite.description

            // Загрузка изображения события с помощью библиотеки Glide
            Glide.with(binding.root.context)
                .load(eventFavourite.imageUrl)
                .placeholder(R.drawable.photonet)
                .error(R.drawable.photonet)
                .into(binding.favouriteEventImage)
        }
    }

    // Callback для определения разницы между старыми и новыми элементами списка
    class EventDiffCallback : DiffUtil.ItemCallback<EventFavourite>() {
        // Сравнение элементов списка по идентификатору события
        override fun areItemsTheSame(
            oldItem: EventFavourite,
            newItem: EventFavourite
        ): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        // Сравнение содержимого элементов списка
        override fun areContentsTheSame(
            oldItem: EventFavourite,
            newItem: EventFavourite
        ): Boolean {
            return oldItem == newItem
        }
    }
}
