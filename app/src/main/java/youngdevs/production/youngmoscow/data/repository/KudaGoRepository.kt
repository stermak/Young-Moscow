package youngdevs.production.youngmoscow.data.repository

import youngdevs.production.youngmoscow.data.dao.KudaGoApi
import youngdevs.production.youngmoscow.data.entities.Event

class KudaGoRepository(private val api: KudaGoApi) {

    suspend fun getEvents(
        pageSize: Int,
        page: Int,
    ): List<Event> {
        val response = api.getEvents(
            location = "msk",
            pageSize = pageSize,
            page = page,
            order_by = "-publication_date"
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
