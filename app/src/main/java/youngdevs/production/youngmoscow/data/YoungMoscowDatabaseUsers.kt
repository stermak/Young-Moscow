package youngdevs.production.youngmoscow.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import youngdevs.production.youngmoscow.data.dao.UserDao
import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.data.utilities.Converters

// Этот класс представляет базу данных Room, которая используется для хранения данных о пользователях.
@Database(
    entities = [User::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class YoungMoscowDatabase : RoomDatabase() {

    // Эта функция возвращает объект UserDao, который используется для выполнения операций с пользователями в базе данных.
    abstract fun userDao(): UserDao
}
