package youngdevs.production.youngmoscow.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import youngdevs.production.youngmoscow.data.entities.FavoriteEvent

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteEvent(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun deleteFavoriteEvent(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM favorite_event_table WHERE event_id = :eventId")
    suspend fun getFavoriteEventById(eventId: Long): FavoriteEvent?

    @Query("SELECT * FROM favorite_event_table")
    fun getFavoriteEvents(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_event_table WHERE id = :eventId")
    suspend fun getEventById(eventId: Long): FavoriteEvent?
}