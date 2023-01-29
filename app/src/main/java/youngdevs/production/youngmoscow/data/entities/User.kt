package youngdevs.production.youngmoscow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import youngdevs.production.youngmoscow.domain.models.UserModel

@Entity
data class User(
    @PrimaryKey
    var id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "email")
    var email: String
)

fun User.asDomainModel(): UserModel {
    return UserModel(
        id = this.id,
        name = this.name,
        email = this.email
    )
}
