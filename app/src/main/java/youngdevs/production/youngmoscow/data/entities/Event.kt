package youngdevs.production.youngmoscow.data.entities

import android.os.Parcelable
import android.text.Html
import androidx.core.text.HtmlCompat
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val images: List<Image>,
    val dates: List<Date>,
    val place: Place,
    val body_text: String,
    val price: String
) : Parcelable {

    val formattedDescription: String
        get() = Html.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    val formattedBodyText: String
        get() = Html.fromHtml(body_text, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    val formattedTitle: String
        get() = Html.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()


    @Parcelize
    data class Image(
        val image: String
    ) : Parcelable

    @Parcelize
    data class Date(
        val start: Long,
        val end: Long
    ) : Parcelable

    @Parcelize
    data class Place(
        val id: Int,
        val title: String,
        val address: String,
        val location: Location
    ) : Parcelable {

        @Parcelize
        data class Location(
            val lat: Double,
            val lon: Double
        ) : Parcelable
    }
}
