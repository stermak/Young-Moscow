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

    // Проверяет, существует ли уже аккаунт с указанным email
    // Возвращает значение типа Boolean, которое указывает на существование аккаунта
    suspend fun checkAccountExists(email: String): Boolean

    // Аутентифицирует пользователя в приложении, используя указанный email и пароль
    // Возвращает значение типа Boolean, которое указывает на успешность операции
    suspend fun authenticate(email: String, password: String): Boolean

    // Получает информацию о текущем пользователе
    // Возвращает объект типа UserModel или null, если пользователь не найден
    suspend fun getCurrentUser(): UserModel?

    // Создает аккаунт с использованием Google
    suspend fun createAccountWithGoogle(email: String, name: String): Boolean

    // Обновляет пароль пользователя
    suspend fun updateUserPassword(newPassword: String)

    // Аутентифицирует пользователя с использованием Google
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

    // Повторно аутентифицирует пользователя
    suspend fun reauthenticate(email: String, password: String): Boolean
}
