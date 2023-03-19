package youngdevs.production.youngmoscow.data.dao

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import youngdevs.production.youngmoscow.data.entities.Event
import youngdevs.production.youngmoscow.data.utilities.ApiResponse

interface KudaGoApi {
    @GET("events/")
    suspend fun getEvents(
        @Query("fields") fields: String = "id,title,description,images,dates,place,body_text,price",
        @Query("actual_since") actualSince: Long,
        @Query("location") location: String? = null
    ): Response<ApiResponse>

    @GET("events/{event_id}/")
    suspend fun getEvent(@Path("event_id") eventId: Int): Response<Event>
}
