package youngdevs.production.youngmoscow.domain.models

// Это модель данных, которая представляет пользователя в приложении
data class UserModel(
    val id: String, // Уникальный идентификатор пользователя
    val name: String? = null, // Имя пользователя. Может быть null
    val email: String? = null, // Email пользователя. Может быть null
    val phone: String? = null, // Phone пользователя. Может быть null
    val profileImage: String? = null
)

