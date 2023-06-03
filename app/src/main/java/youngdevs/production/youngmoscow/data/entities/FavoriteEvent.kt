package youngdevs.production.youngmoscow.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_event_table")
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val address: String,
    val image: String,

    @ColumnInfo(name = "event_id")
    val eventId: Long
)