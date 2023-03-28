package youngdevs.production.youngmoscow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import youngdevs.production.youngmoscow.domain.models.UserModel

@Entity // Аннотация, которая указывает, что это класс, который будет использоваться в базе данных Room

data class User( // Класс, описывающий пользователя
    @PrimaryKey // Указывает, что это поле является первичным ключом таблицы в базе данных
    var id: String, // Уникальный идентификатор пользователя

    @ColumnInfo(name = "name") // Аннотация, которая указывает, что это поле будет использоваться в качестве столбца в таблице базы данных
    var name: String, // Имя пользователя

    @ColumnInfo(name = "email") // Аннотация, которая указывает, что это поле будет использоваться в качестве столбца в таблице базы данных
    var email: String, // Адрес электронной почты пользователя

    @ColumnInfo(name = "phone")
    var phone: String?

)

fun User.asDomainModel(): UserModel { // Функция-расширение для класса User, которая конвертирует объект User в объект UserModel
    return UserModel(
        id = this.id, // Уникальный идентификатор пользователя
        name = this.name, // Имя пользователя
        email = this.email, // Адрес электронной почты пользователя
        phone = this.phone
    )
}