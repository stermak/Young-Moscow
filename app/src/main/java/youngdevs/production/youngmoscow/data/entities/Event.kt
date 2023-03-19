package youngdevs.production.youngmoscow.data.entities

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val images: List<Image>
)

data class Image(
    val image: String
)
