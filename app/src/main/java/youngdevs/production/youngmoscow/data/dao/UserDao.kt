package youngdevs.production.youngmoscow.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import youngdevs.production.youngmoscow.common.USER_TABLE
import youngdevs.production.youngmoscow.data.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM $USER_TABLE ORDER BY id ASC")
    fun readAllData(): Flow<List<User>>

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM user_data")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM user_data WHERE name LIKE :name AND " +
            "email LIKE :email LIMIT 1")
    fun findByName(name: String, email: String): User
}
