package youngdevs.production.youngmoscow.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import youngdevs.production.youngmoscow.data.dao.FavoriteEventDao
import youngdevs.production.youngmoscow.data.entities.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "favorite_database"
        ).build()
    }

    @Provides
    fun provideFavoriteEventDao(database: AppDatabase): FavoriteEventDao {
        return database.favoriteEventDao()
    }
}
