package youngdevs.production.youngmoscow.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.databinding.ItemEventBinding

class EventsAdapter(private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {
    private val events = mutableListOf<Event>() // список событий для адаптера

    interface OnItemClickListener {
        fun onItemClick(event: Event) // интерфейс для реализации обработчика клика на элементе списка
    }

    // метод для установки списка событий для адаптера
    fun setEvents(newEvents: List<Event>) {
        events.addAll(newEvents)
        notifyDataSetChanged()
    }


    // создание ViewHolder для элемента списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false) // раздувание макета элемента списка
        return EventViewHolder(binding) // возвращение ViewHolder для элемента списка
    }

    // привязка данных к ViewHolder
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position] // получение текущего события
        holder.bind(event) // привязка данных события к ViewHolder

        holder.itemView.setOnClickListener { // установка обработчика клика на элемент списка
            onItemClickListener.onItemClick(event) // вызов метода из интерфейса для обработки клика на элементе списка
        }
    }

    override fun getItemCount() = events.size // возвращает количество элементов в списке

    // класс ViewHolder для элемента списка
    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        // метод для привязки данных события к элементу списка
        fun bind(event: Event) {
            binding.eventTitle.text = event.formattedTitle
            binding.eventDescription.text = event.formattedDescription

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
        }
    }
}
