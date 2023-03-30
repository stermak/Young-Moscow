package youngdevs.production.youngmoscow.data.utilities

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import youngdevs.production.youngmoscow.data.entities.Sightseeing

interface SightseeingsService {
    @GET("/api/sightseeings")
    suspend fun getSightseeings(): List<Sightseeing>
}

interface ImagesService {
    @GET("/api/images/{imageName}")
    suspend fun getImage(@Path("imageName") imageName: String): Response<ResponseBody>
}

object RetrofitClient {
    private const val BASE_URL = "http://188.244.41.200:12345/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val sightseeingsService: SightseeingsService by lazy {
        retrofit.create(SightseeingsService::class.java)
    }

    val imagesService: ImagesService by lazy {
        retrofit.create(ImagesService::class.java)
    }
}
