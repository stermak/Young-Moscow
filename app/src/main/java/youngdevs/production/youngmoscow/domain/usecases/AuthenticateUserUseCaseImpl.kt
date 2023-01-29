package youngdevs.production.youngmoscow.domain.usecases

import youngdevs.production.youngmoscow.domain.models.UserModel
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import javax.inject.Inject


class AuthenticateUserUseCaseImpl @Inject constructor(private val userRepository: UserRepository) :
    AuthenticateUserUseCase {

    override suspend fun signIn(email: String, password: String): Boolean {
        return if (email != "" && password != "")
            userRepository.authenticate(email, password)
        else
            false
    }

    override suspend fun getCurrentUser(): UserModel? {
        return userRepository.getCurrentUser()
    }

    override suspend fun signOut() {
        userRepository.clearUser()
    }

    override suspend fun createAccount(
        email: String,
        password: String,
        repeatPassword: String,
        name: String
    ): Int {
        return if (password == repeatPassword) {
            if (userRepository.createAccount(email, password, name)) {
                1
            } else
                0
        } else
            -1

    }

}