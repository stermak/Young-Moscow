package youngdevs.production.youngmoscow.domain.usecases

import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.data.repository.UserRepositoryImpl
import javax.inject.Inject

class AddUserUseCase @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl){

    suspend fun addUser(user: User){
        userRepositoryImpl.addUser(user)
    }

}