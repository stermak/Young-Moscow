package youngdevs.production.youngmoscow.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// AppModule - модуль, предоставляющий зависимости для всего приложения
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Функция, которая предоставляет контекст приложения для зависимостей
    @Provides
    fun provideAppContext(application: Application): Context {
        return application.applicationContext
    }
}
