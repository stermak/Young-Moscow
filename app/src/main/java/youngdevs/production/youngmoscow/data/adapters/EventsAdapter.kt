package youngdevs.production.youngmoscow.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import youngdevs.production.youngmoscow.R
import youngdevs.production.youngmoscow.data.entities.Event

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private val events = mutableListOf<Event>()

    fun setEvents(newEvents: List<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventTitle = itemView.findViewById<TextView>(R.id.eventTitle)
        private val eventDescription = itemView.findViewById<TextView>(R.id.eventDescription)

        fun bind(event: Event) {
            eventTitle.text = event.title
            eventDescription.text = event.description
        }
    }
}
