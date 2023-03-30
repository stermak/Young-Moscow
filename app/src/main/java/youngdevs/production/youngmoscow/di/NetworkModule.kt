package youngdevs.production.youngmoscow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import youngdevs.production.youngmoscow.data.services.ImagesService
import youngdevs.production.youngmoscow.data.services.RetrofitClient
import youngdevs.production.youngmoscow.data.services.SightseeingsService
import javax.inject.Singleton

// Определение модуля NetworkModule для предоставления зависимостей, связанных с сетью
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // Предоставление зависимости SightseeingsService в виде объекта RetrofitClient.sightseeingsService
    @Provides
    @Singleton
    fun provideSightseeingsService(): SightseeingsService {
        return RetrofitClient.sightseeingsService
    }

    // Предоставление зависимости ImagesService в виде объекта RetrofitClient.imagesService
    @Provides
    @Singleton
    fun provideImagesService(): ImagesService {
        return RetrofitClient.imagesService
    }
}