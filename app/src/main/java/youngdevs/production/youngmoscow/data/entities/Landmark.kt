package youngdevs.production.youngmoscow.data.entities

data class Landmark(
    val name: String,
    val location: Location
)

data class Location(
    val latitude: Double,
    val longitude: Double
)

