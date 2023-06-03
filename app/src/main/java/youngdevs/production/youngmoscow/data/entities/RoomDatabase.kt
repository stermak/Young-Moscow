package youngdevs.production.youngmoscow.data.entities

import androidx.room.Database
import androidx.room.RoomDatabase
import youngdevs.production.youngmoscow.data.dao.FavoriteEventDao

@Database(entities = [FavoriteEvent::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteEventDao(): FavoriteEventDao
}