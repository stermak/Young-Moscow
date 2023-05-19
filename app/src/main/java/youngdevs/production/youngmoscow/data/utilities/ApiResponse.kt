package youngdevs.production.youngmoscow.data.utilities

import youngdevs.production.youngmoscow.data.entities.Event

// Этот класс является моделью ответа API и содержит информацию о количестве результатов, ссылках на предыдущую и следующую страницы и список событий.
data class ApiResponse(
    val count: Int,       // Количество результатов
    val next: String?,    // Ссылка на следующую страницу
    val previous: String?,// Ссылка на предыдущую страницу
    val results: List<Event> // Список событий
)
