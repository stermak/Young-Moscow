package youngdevs.production.youngmoscow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    // Функция, которая предоставляет экземпляр AuthenticateUserUseCase для зависимостей
    @Provides
    @Singleton
    fun provideAuthenticateUserUseCase(
        userRepository: UserRepository
    ): AuthenticateUserUseCase {
        return AuthenticateUserUseCaseImpl(userRepository = userRepository)
    }
}
