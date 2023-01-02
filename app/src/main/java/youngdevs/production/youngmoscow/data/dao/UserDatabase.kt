package youngdevs.production.youngmoscow.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import youngdevs.production.youngmoscow.data.entities.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase :RoomDatabase(){

    abstract fun userDao(): UserDao
}