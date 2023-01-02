package youngdevs.production.youngmoscow.domain.repository

import kotlinx.coroutines.flow.Flow
import youngdevs.production.youngmoscow.data.entities.User

interface Repository {

    suspend fun readData() : Flow<List<User>>

}