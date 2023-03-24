package youngdevs.production.youngmoscow.data.entities

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import youngdevs.production.youngmoscow.data.dao.FavouriteEventsDao

@Entity(tableName = "favourite_events")
data class EventFavourite(
    @PrimaryKey
    val eventId: Int,
    val title: String,
    val description: String,
    val imageUrl: String?
)

@Database(entities = [EventFavourite::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {
    abstract fun favouriteEventsDao(): FavouriteEventsDao
}
