package youngdevs.production.youngmoscow.data.repository

import youngdevs.production.youngmoscow.data.dao.KudaGoApi
import youngdevs.production.youngmoscow.data.entities.Event

class KudaGoRepository(private val api: KudaGoApi) {
    suspend fun getEvents(): List<Event> {
        return api.getEvents().body()?.results ?: emptyList()
    }
}

