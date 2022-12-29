package youngdevs.production.youthmoscow.dao

import androidx.room.*

import kotlinx.coroutines.flow.Flow
import youngdevs.production.youthmoscow.entities.User
import youngdevs.production.youthmoscow.utilities.USER_TABLE

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM $USER_TABLE WHERE id = :id")
    fun getById(id: String): Flow<User>

    @Query("SELECT * FROM $USER_TABLE LIMIT 1")
    suspend fun getCurrentUser(): User?

    @Query("DELETE FROM  $USER_TABLE")
    suspend fun clear()
}