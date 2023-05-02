package youngdevs.production.youngmoscow.domain.repository

import youngdevs.production.youngmoscow.domain.models.UserModel

interface UserRepository {
    // Создает новый аккаунт пользователя в приложении, используя указанный email, пароль и имя
    // Возвращает значение типа Boolean, которое указывает на успешность операции
    suspend fun createAccount(
        email: String,
        password: String,
        name: String,
        phone: String? = null
    ): Boolean

    // Аутентифицирует пользователя в приложении, используя указанный email и пароль
    // Возвращает значение типа Boolean, которое указывает на успешность операции
    suspend fun authenticate(email: String, password: String): Boolean

    // Получает информацию о текущем пользователе
    // Возвращает объект типа UserModel или null, если пользователь не найден
    suspend fun getCurrentUser(): UserModel?

    suspend fun updateUserPassword(newPassword: String)

    suspend fun authenticateWithGoogle(idToken: String): Boolean

    // Обновляет профиль пользователя с указанным идентификатором, используя указанное имя и email
    suspend fun updateUserProfile(
        userId: String,
        name: String,
        email: String,
        phone: String
    )

    // Удаляет информацию о текущем пользователе
    suspend fun clearUser()
}
