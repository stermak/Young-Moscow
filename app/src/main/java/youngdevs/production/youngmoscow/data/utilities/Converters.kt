package youngdevs.production.youngmoscow.data.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {
    @TypeConverter
    fun converterListOfListToJson(list: List<List<String>>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun converterJsonToListOfList(string: String): List<List<String>> {
        return Gson().fromJson(
            string,
            object : TypeToken<List<List<String>>>() {}.type
        )
    }

    @TypeConverter
    fun converterListToJson(list: List<String>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun converterJsonToList(string: String): List<String> {
        return Gson().fromJson(
            string,
            object : TypeToken<List<String>>() {}.type
        )
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}