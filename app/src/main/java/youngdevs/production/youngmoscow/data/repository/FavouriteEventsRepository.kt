package youngdevs.production.youngmoscow.data.repository

import javax.inject.Singleton
import youngdevs.production.youngmoscow.data.dao.FavouriteEventsDao
import youngdevs.production.youngmoscow.data.entities.EventFavourite

// Определение класса репозитория FavouriteEventsRepository, который обеспечивает доступ к данным
// и предоставляет методы для работы с таблицей EventFavourite в базе данных
@Singleton
class FavouriteEventsRepository(
    private val favouriteEventsDao: FavouriteEventsDao
) {
    // Получение всех избранных событий из базы данных с помощью метода getAllFavouriteEvents()
    val favouriteEvents = favouriteEventsDao.getAllFavouriteEvents()

    // Добавление нового события в список избранных с помощью метода addToFavourites()
    suspend fun addToFavourites(event: EventFavourite) =
        favouriteEventsDao.addToFavourites(event)

    // Удаление события из списка избранных по его идентификатору с помощью метода
    // removeFromFavourites()
    suspend fun removeFromFavourites(eventId: Int) =
        favouriteEventsDao.removeFromFavourites(eventId)

    // Получение события из таблицы EventFavourite по его идентификатору с помощью метода
    // getEventById()
    suspend fun getEventById(eventId: Int): EventFavourite? {
        return favouriteEventsDao.getEventById(eventId)
    }
}
