package youngdevs.production.youngmoscow.data.repository

import youngdevs.production.youngmoscow.data.dao.KudaGoApi
import youngdevs.production.youngmoscow.data.entities.Event
import java.util.*

class KudaGoRepository(private val api: KudaGoApi) {

    suspend fun getEvents(
        pageSize: Int,
        page: Int,
        actualSince: Long,
        actualUntil: Long
    ): List<Event> {
        val response = api.getEvents(
            actualSince = actualSince,
            actualUntil = actualUntil,
            location = "msk",
            pageSize = pageSize,
            page = page
        )

        return response.body()?.results ?: emptyList()
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
