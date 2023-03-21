package youngdevs.production.youngmoscow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import youngdevs.production.youngmoscow.data.dao.KudaGoApi
import youngdevs.production.youngmoscow.data.repository.KudaGoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class KudaGoModule {

    // Функция, которая предоставляет экземпляр API для зависимостей
    @Provides
    @Singleton
    fun provideKudaGoApi(): KudaGoApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://kudago.com/public-api/v1.4/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(KudaGoApi::class.java)
    }

    // Функция, которая предоставляет экземпляр репозитория для зависимостей
    @Provides
    @Singleton
    fun provideKudaGoRepository(api: KudaGoApi): KudaGoRepository {
        return KudaGoRepository(api)
    }
}

