package youngdevs.production.youngmoscow.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Long,
    val name: String,
    val description: String,
    val address: String,
    val image: String
) : Parcelable
