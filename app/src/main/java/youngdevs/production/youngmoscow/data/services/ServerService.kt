package youngdevs.production.youngmoscow.data.services

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.entities.Sightseeing

// Определение интерфейса SightseeingsService для работы с API, предоставляющего информацию о
// достопримечательностях
interface SightseeingsService {
    @GET("/api/sightseeings")
    suspend fun getSightseeings(): List<Sightseeing>
}

// Определение интерфейса ImagesService для работы с API, предоставляющего доступ к изображениям
interface ImagesService {
    @GET("/api/images/{imageName}")
    suspend fun getImage(
        @Path("imageName") imageName: String
    ): Response<ResponseBody>
}

interface EventsService {
    @GET("/api/events")
    suspend fun getEvents(): List<Event>
}

interface ImagesEventsService {
    @GET("/api/imagesEvents/{imageEventName}")
    suspend fun getImageEvent(
        @Path("imageEventName") imageEventName: String
    ): Response<ResponseBody>
}

// Объект RetrofitClient предоставляет доступ к Retrofit, который используется для выполнения
// запросов к API
object RetrofitClient {
    private const val BASE_URL = "http://213.171.9.155:31820/"

    // Ленивая инициализация Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Создание объекта SightseeingsService, который будет использоваться для выполнения запросов к
    // API достопримечательностей
    val sightseeingsService: SightseeingsService by lazy {
        retrofit.create(SightseeingsService::class.java)
    }

    // Создание объекта ImagesService, который будет использоваться для выполнения запросов к API
    // изображений
    val imagesService: ImagesService by lazy {
        retrofit.create(ImagesService::class.java)
    }

    val eventsService: EventsService by lazy {
        retrofit.create(EventsService::class.java)
    }

    val imagesEventsService: ImagesEventsService by lazy {
        retrofit.create(ImagesEventsService::class.java)
    }
}
