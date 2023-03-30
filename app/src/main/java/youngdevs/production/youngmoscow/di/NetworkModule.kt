package youngdevs.production.youngmoscow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import youngdevs.production.youngmoscow.data.utilities.ImagesService
import youngdevs.production.youngmoscow.data.utilities.RetrofitClient
import youngdevs.production.youngmoscow.data.utilities.SightseeingsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSightseeingsService(): SightseeingsService {
        return RetrofitClient.sightseeingsService
    }

    @Provides
    @Singleton
    fun provideImagesService(): ImagesService {
        return RetrofitClient.imagesService
    }
}
