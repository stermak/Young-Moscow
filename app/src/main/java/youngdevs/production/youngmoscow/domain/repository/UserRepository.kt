package youngdevs.production.youngmoscow.domain.repository

import youngdevs.production.youngmoscow.domain.models.UserModel

interface UserRepository {
    suspend fun createAccount(email: String, password: String, name: String): Boolean
    suspend fun authenticate(email: String, password: String): Boolean
    suspend fun getCurrentUser(): UserModel?
    suspend fun updateUserProfile(userId: String, name: String, email: String)
    suspend fun clearUser()
}