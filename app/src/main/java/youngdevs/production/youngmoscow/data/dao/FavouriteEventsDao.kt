package youngdevs.production.youngmoscow.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import youngdevs.production.youngmoscow.data.entities.EventFavourite

@Dao
interface FavouriteEventsDao {
    @Query("SELECT * FROM favourite_events")
    fun getAllFavouriteEvents(): LiveData<List<EventFavourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourites(event: EventFavourite)

    @Query("DELETE FROM favourite_events WHERE eventId = :eventId")
    suspend fun removeFromFavourites(eventId: Int)

    @Query("SELECT * FROM favourite_events WHERE eventId = :eventId")
    suspend fun getEventById(eventId: Int): EventFavourite? // Добавлен новый метод
}
