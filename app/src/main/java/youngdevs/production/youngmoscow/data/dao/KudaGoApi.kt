package youngdevs.production.youngmoscow.data.dao

import retrofit2.http.GET
import youngdevs.production.youngmoscow.data.entities.ApiResponse
import youngdevs.production.youngmoscow.data.entities.Event

interface KudaGoApi {
    @GET("events/")
    suspend fun getEvents(): retrofit2.Response<ApiResponse>
}

