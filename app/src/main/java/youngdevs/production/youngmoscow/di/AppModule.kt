package youngdevs.production.youngmoscow.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import youngdevs.production.youngmoscow.data.dao.FavouriteEventsDao
import youngdevs.production.youngmoscow.data.entities.FavouriteDatabase
import youngdevs.production.youngmoscow.data.repository.FavouriteEventsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFavouriteEventsDao(database: FavouriteDatabase): FavouriteEventsDao {
        return database.favouriteEventsDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): FavouriteDatabase {
        return Room.databaseBuilder(
            appContext,
            FavouriteDatabase::class.java,
            "FavouriteDatabase"
        ).build()
    }

    @Singleton
    @Provides
    fun provideFavouriteEventsRepository(favouriteEventsDao: FavouriteEventsDao): FavouriteEventsRepository {
        return FavouriteEventsRepository(favouriteEventsDao)
    }

    // Функция, которая предоставляет контекст приложения для зависимостей
    @Provides
    fun provideAppContext(application: Application): Context {
        return application.applicationContext
    }
}
