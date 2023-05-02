package youngdevs.production.youngmoscow.data.repository

import youngdevs.production.youngmoscow.data.dao.KudaGoApi
import youngdevs.production.youngmoscow.data.entities.Event

// Определение класса репозитория KudaGoRepository, который обеспечивает доступ к данным, полученным
// из API
class KudaGoRepository(private val api: KudaGoApi) {

    // Функция для получения списка событий с помощью API
    // pageSize - количество элементов, выводимых на странице, page - номер страницы
    suspend fun getEvents(
        pageSize: Int,
        page: Int,
    ): List<Event> {
        // Выполнение запроса к API и получение ответа
        val response =
            api.getEvents(
                location = "msk", // Задание географического местоположения
                pageSize =
                pageSize, // Задание количества элементов на странице
                page = page, // Задание номера страницы
                order_by =
                "-publication_date" // Задание порядка сортировки по дате публикации
            )

        // Возвращение списка событий из ответа или пустого списка, если ответ не содержит данных
        return response.body()?.results ?: emptyList()
    }

    // Функция для получения подробной информации о событии с помощью API
    // eventId - идентификатор события
    suspend fun getEventDetails(eventId: Int): Event? {
        // Выполнение запроса к API и получение ответа
        val response = api.getEvent(eventId)

        // Если ответ успешный, то возвращается объект события, иначе - null
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
