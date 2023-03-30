package youngdevs.production.youngmoscow.data.entities

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import youngdevs.production.youngmoscow.data.dao.FavouriteEventsDao

// Определение класса EventFavourite, который будет использоваться в базе данных
@Entity(tableName = "favourite_events")
data class EventFavourite(
    @PrimaryKey
    val eventId: Int, // Поле, содержащее идентификатор события
    val title: String, // Поле, содержащее заголовок события
    val description: String, // Поле, содержащее описание события
    val imageUrl: String? // Поле, содержащее ссылку на изображение события (может быть null)
)

// Определение класса базы данных FavouriteDatabase, который содержит таблицу EventFavourite
@Database(entities = [EventFavourite::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {
    // Абстрактная функция, возвращающая объект доступа к данным для таблицы EventFavourite
    abstract fun favouriteEventsDao(): FavouriteEventsDao
}
