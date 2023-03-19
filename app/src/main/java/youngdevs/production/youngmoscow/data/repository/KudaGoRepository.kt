package youngdevs.production.youngmoscow.data.repository

import youngdevs.production.youngmoscow.data.dao.KudaGoApi
import youngdevs.production.youngmoscow.data.entities.Event
import java.util.*

class KudaGoRepository(private val api: KudaGoApi) {
    suspend fun getEvents(): List<Event> {
        val currentTime = Calendar.getInstance().timeInMillis / 1000
        return api.getEvents(actualSince = currentTime, location = "msk").body()?.results ?: emptyList()
    }
    suspend fun getEventDetails(eventId: Int): Event? {
        val response = api.getEvent(eventId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
