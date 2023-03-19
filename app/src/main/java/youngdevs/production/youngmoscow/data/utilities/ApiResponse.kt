package youngdevs.production.youngmoscow.data.utilities

import youngdevs.production.youngmoscow.data.entities.Event

data class ApiResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Event>
)
