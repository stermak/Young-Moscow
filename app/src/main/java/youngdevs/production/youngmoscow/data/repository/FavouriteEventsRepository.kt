package youngdevs.production.youngmoscow.data.repository

import youngdevs.production.youngmoscow.data.dao.FavouriteEventsDao
import youngdevs.production.youngmoscow.data.entities.EventFavourite
import javax.inject.Singleton

@Singleton
class FavouriteEventsRepository(private val favouriteEventsDao: FavouriteEventsDao) {
    val favouriteEvents = favouriteEventsDao.getAllFavouriteEvents()

    suspend fun addToFavourites(event: EventFavourite) = favouriteEventsDao.addToFavourites(event)

    suspend fun removeFromFavourites(eventId: Int) = favouriteEventsDao.removeFromFavourites(eventId)

    suspend fun getEventById(eventId: Int): EventFavourite? {
        return favouriteEventsDao.getEventById(eventId)
    }
}
