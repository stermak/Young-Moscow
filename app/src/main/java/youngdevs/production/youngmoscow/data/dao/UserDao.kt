package youngdevs.production.youngmoscow.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.data.utilities.USER_TABLE

@Dao
interface UserDao {
    // Аннотация @Dao указывает, что это интерфейс для работы с базой данных Room.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
    // Метод insert() добавляет пользователя в базу данных Room.
    // Аннотация @Insert указывает, что это операция вставки.
    // Аргумент onConflict указывает, что нужно сделать в случае конфликта, т.е. при попытке вставки
    // уже существующей записи.
    // В данном случае указано заменить старую запись на новую.

    @Query(
        "UPDATE user SET name = :name, email = :email, phone = :phone WHERE id = :userId"
    )
    suspend fun updateUser(
        userId: String,
        name: String,
        email: String,
        phone: String
    )

    @Query("SELECT * FROM $USER_TABLE WHERE id = :id")
    fun getById(id: String): Flow<User>
    // Метод getById() получает пользователя из базы данных Room по заданному ID.
    // Аннотация @Query указывает SQL-запрос для получения данных.
    // Аргумент id - это значение, которое будет подставлено в запрос.

    @Query("SELECT * FROM $USER_TABLE LIMIT 1")
    suspend fun getCurrentUser(): User?
    // Метод getCurrentUser() получает текущего пользователя из базы данных Room.
    // Аннотация @Query указывает SQL-запрос для получения данных.

    @Query("DELETE FROM  $USER_TABLE") suspend fun clear()
    // Метод clear() удаляет все данные о пользователях из базы данных Room.
    // Аннотация @Query указывает SQL-запрос для удаления данных.
}
