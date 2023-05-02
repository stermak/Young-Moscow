package youngdevs.production.youngmoscow.data.dao

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.utilities.ApiResponse

interface KudaGoApi {
    // Интерфейс KudaGoApi объявляет два метода для работы с API KudaGo.

    @GET(
        "events/"
    ) // Метод getEvents() отправляет GET запрос на сервер KudaGo для получения списка событий.
    // Аннотация @Query указывает параметры запроса, в данном случае: fields, actual_since и
    // location.
    suspend fun getEvents(
        @Query("fields")
        fields: String =
            "id,title,description,images,site_url,body_text,price",
        @Query("location") location: String? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("page") page: Int? = null,
        @Query("order_by") order_by: String? = null
    ): Response<ApiResponse>

    @GET("events/{event_id}/")
    suspend fun getEvent(@Path("event_id") eventId: Int): Response<Event>
}
// Метод getEvent() отправляет GET запрос на сервер KudaGo для получения информации о событии по ID.
// Аннотация @Path указывает переменную в URL запроса, которая будет заменена на значение переменной
// eventId.
