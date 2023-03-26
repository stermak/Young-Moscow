package youngdevs.production.youngmoscow.di

import com.google.firebase.auth.FirebaseAuth
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

    // Функция, которая предоставляет экземпляр FirebaseAuth для зависимостей
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // Функция, которая предоставляет экземпляр UserRepository для зависимостей
    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, firebaseAuth: FirebaseAuth): UserRepository {
        return UserRepositoryImpl(userDao, firebaseAuth)
    }
}
