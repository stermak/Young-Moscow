package youngdevs.production.youngmoscow.domain.usecases

import kotlinx.coroutines.flow.Flow
import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.data.repository.UserRepositoryImpl
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl) {

    suspend fun getData(): Flow<List<User>> = userRepositoryImpl.readAllData

    val readAllData: Flow<List<User>> = userRepositoryImpl.readAllData
}