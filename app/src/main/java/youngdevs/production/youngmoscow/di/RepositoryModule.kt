package youngdevs.production.youngmoscow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import youngdevs.production.youngmoscow.data.dao.UserDao
import youngdevs.production.youngmoscow.data.repository.UserRepositoryImpl
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    // Функция, которая предоставляет экземпляр UserRepository для зависимостей
    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }
}
