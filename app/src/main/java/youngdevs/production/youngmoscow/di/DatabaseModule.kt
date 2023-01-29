package youngdevs.production.youngmoscow.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import youngdevs.production.youngmoscow.data.YoungMoscowDatabase
import youngdevs.production.youngmoscow.data.dao.UserDao
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(appContext: Context): YoungMoscowDatabase {
        return Room.databaseBuilder(
            appContext,
            YoungMoscowDatabase::class.java,
            "young_moscow.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: YoungMoscowDatabase): UserDao {
        return database.userDao()
    }

}