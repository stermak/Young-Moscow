package youngdevs.production.youngmoscow.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import youngdevs.production.youngmoscow.data.entities.EventFavourite

// Интерфейс для работы с таблицей избранных событий в базе данных
@Dao
interface FavouriteEventsDao {
    // Запрос на выборку всех записей из таблицы favourite_events
    // Возвращает LiveData со списком избранных событий
    @Query("SELECT * FROM favourite_events")
    fun getAllFavouriteEvents(): LiveData<List<EventFavourite>>

    // Вставка события в таблицу favourite_events
    // Если событие с таким же идентификатором уже существует, оно будет заменено
    // (OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourites(event: EventFavourite)

    // Удаление события из таблицы favourite_events по идентификатору события
    @Query("DELETE FROM favourite_events WHERE eventId = :eventId")
    suspend fun removeFromFavourites(eventId: Int)

    // Запрос на выборку события из таблицы favourite_events по идентификатору события
    // Возвращает найденное событие или null, если событие не найдено
    @Query("SELECT * FROM favourite_events WHERE eventId = :eventId")
    suspend fun getEventById(eventId: Int): EventFavourite?
}
