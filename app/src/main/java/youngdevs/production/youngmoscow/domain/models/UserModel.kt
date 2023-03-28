package youngdevs.production.youngmoscow.domain.models

// Это модель данных, которая представляет пользователя в приложении
data class UserModel(
    val id: String, // Уникальный идентификатор пользователя
    val name: String? = null, // Имя пользователя. Может быть null
    val email: String?, // Email пользователя. Обязателен для заполнения
    val phone: String? = null // Phone пользователя.
)
