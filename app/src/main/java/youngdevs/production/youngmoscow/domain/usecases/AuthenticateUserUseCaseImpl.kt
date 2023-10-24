package youngdevs.production.youngmoscow.domain.usecases

import youngdevs.production.youngmoscow.domain.models.UserModel
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import javax.inject.Inject

class AuthenticateUserUseCaseImpl
@Inject
constructor(private val userRepository: UserRepository) :
    AuthenticateUserUseCase {

    // Аутентифицирует пользователя с указанным email и паролем в приложении
    // Возвращает значение типа Boolean, которое указывает на успешность операции
    override suspend fun signIn(email: String, password: String): Boolean {
        return if (email != "" && password != "")
            userRepository.authenticate(email, password)
        else false
    }

    // Получает информацию о текущем пользователе
    // Возвращает объект типа UserModel или null, если пользователь не найден
    override suspend fun getCurrentUser(): UserModel? {
        return userRepository.getCurrentUser()
    }

    override suspend fun signInWithGoogle(idToken: String): Boolean {
        return userRepository.authenticateWithGoogle(idToken)
    }

    // Выходит из учетной записи пользователя
    override suspend fun signOut() {
        userRepository.clearUser()
    }

    // Создает новую учетную запись пользователя в приложении, используя указанный email, пароль,
    // имя и подтверждение пароля
    // Возвращает код результата, который может иметь следующие значения:
    // 1 - успешное создание учетной записи
    // 0 - не удалось создать учетную запись из-за ошибок валидации данных
    // -1 - не удалось создать учетную запись из-за других ошибок
    override suspend fun createAccount(
        email: String,
        password: String,
        name: String
    ): Int {
        try {
            return if (userRepository.createAccount(email, password, name)) {
                1
            } else {
                0
            }
        } catch (e: Exception) {
            println("Error creating account: ${e.message}")
            return -1
        }
    }


    override suspend fun checkAccountExists(email: String): Boolean {
        return userRepository.checkAccountExists(email)
    }

}