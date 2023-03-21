package youngdevs.production.youngmoscow.data.repository

import youngdevs.production.youngmoscow.data.dao.KudaGoApi
import youngdevs.production.youngmoscow.data.entities.Event
import java.util.*

class KudaGoRepository(private val api: KudaGoApi) {
    // Класс для получения данных о мероприятиях из API KudaGo.

    // Функция для получения списка мероприятий из API.
    suspend fun getEvents(): List<Event> {
        // Получаем текущее время в миллисекундах и делим его на 1000, чтобы получить время в секундах.
        val currentTime = Calendar.getInstance().timeInMillis / 1000
        // Вызываем функцию getEvents из объекта api, передавая время, начиная с которого нужно получить мероприятия, и местоположение.
        // Если запрос успешный, то возвращаем список мероприятий, если нет, то пустой список.
        return api.getEvents(actualSince = currentTime, location = "msk").body()?.results ?: emptyList()
    }

    // Функция для получения подробной информации о мероприятии по его ID.
    suspend fun getEventDetails(eventId: Int): Event? {
        // Вызываем функцию getEvent из объекта api, передавая ID мероприятия.
        val response = api.getEvent(eventId)
        // Если запрос успешный, то возвращаем подробную информацию о мероприятии, если нет, то null.
        return if (response.isSuccessful) {
            api.getEvent(eventId).body()
        } else {
            null
        }
    }
}
