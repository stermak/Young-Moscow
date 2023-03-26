package youngdevs.production.youngmoscow.domain.usecases

import youngdevs.production.youngmoscow.domain.models.UserModel


interface AuthenticateUserUseCase {
    // Аутентифицирует пользователя с указанным email и паролем в приложении
    // Возвращает значение типа Boolean, которое указывает на успешность операции
    suspend fun signIn(email: String, password: String): Boolean

    // Получает информацию о текущем пользователе
    // Возвращает объект типа UserModel или null, если пользователь не найден
    suspend fun getCurrentUser(): UserModel?

    // Выходит из учетной записи пользователя
    suspend fun signOut()

    suspend fun signInWithGoogle(idToken: String): Boolean

    // Создает новую учетную запись пользователя в приложении, используя указанный email, пароль, имя и подтверждение пароля
    // Возвращает код результата, который может иметь следующие значения:
    // 1 - успешное создание учетной записи
    // 0 - не удалось создать учетную запись из-за ошибок валидации данных
    // -1 - не удалось создать учетную запись из-за других ошибок
    suspend fun createAccount(
        email: String,
        password: String,
        repeatPassword: String,
        name: String
    ): Int
}
