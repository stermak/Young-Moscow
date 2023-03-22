package youngdevs.production.youngmoscow.data.entities

import android.os.Parcelable
import android.text.Html
import androidx.core.text.HtmlCompat
import kotlinx.parcelize.Parcelize

@Parcelize // Аннотация для автоматической сериализации/десериализации объектов при передаче их между компонентами Android

data class Event( // Основной класс, который описывает событие
    val id: Int, // Уникальный идентификатор события
    val title: String, // Название события
    val description: String, // Описание события
    val images: List<Image>, // Список изображений, связанных с событием
    val site_url: String, // Место, где проходит событие
    val body_text: String, // Текстовое описание события
    val price: String // Цена на событие
) : Parcelable { // Интерфейс для передачи объектов через Android-компоненты
    // Форматирование описания события в текстовый вид
    val formattedDescription: String
        get() = Html.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    // Форматирование текстового описания события в текстовый вид
    val formattedBodyText: String
        get() = Html.fromHtml(body_text, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    // Форматирование названия события в текстовый вид
    val formattedTitle: String
        get() = Html.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    @Parcelize
    data class Image(
        val image: String // URL-адрес изображения
    ) : Parcelable

    @Parcelize
    data class SiteUrl(
        val site_url: String, // Адрес сайта мероприятия
    ) : Parcelable

    @Parcelize
    data class Location(
        val lat: Double, // Широта координат места
        val lon: Double // Долгота координат места
    ) : Parcelable
}

