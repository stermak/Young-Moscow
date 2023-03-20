package youngdevs.production.youngmoscow.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import youngdevs.production.youngmoscow.data.YoungMoscowDatabase
import youngdevs.production.youngmoscow.data.dao.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {



    @Provides
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
