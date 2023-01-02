package youngdevs.production.youngmoscow.domain.usecases

import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.data.repository.UserRepositoryImpl
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl) {

    suspend fun deleteUser(user: User) {
        userRepositoryImpl.deleteUser(user)

    }

    suspend fun deleteAllUsers() {
        userRepositoryImpl.deleteAllUsers()
    }
}