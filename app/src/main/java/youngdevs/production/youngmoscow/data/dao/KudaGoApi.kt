package youngdevs.production.youngmoscow.data.dao

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import youngdevs.production.youngmoscow.data.entities.ApiResponse
import youngdevs.production.youngmoscow.data.entities.Event

interface KudaGoApi {
    @GET("events/")
    suspend fun getEvents(
        @Query("fields") fields: String = "id,title,description,images",
        @Query("actual_since") actualSince: Long,
        @Query("location") location: String? = null
    ): retrofit2.Response<ApiResponse>
    @GET("events/{event_id}/")
    suspend fun getEvent(@Path("event_id") eventId: Int): Response<Event>
}
