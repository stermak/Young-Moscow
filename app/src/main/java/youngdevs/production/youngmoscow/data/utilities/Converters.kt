package youngdevs.production.youngmoscow.data.utilities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

// Этот класс является классом-конвертером для Room, который позволяет Room сохранять и извлекать данные из базы данных в формате, который не поддерживается по умолчанию.
class Converters {
    // Эта функция конвертирует список списков строк в JSON-строку.
    @TypeConverter
    fun converterListOfListToJson(list: List<List<String>>): String? {
        return Gson().toJson(list)
    }

    // Эта функция конвертирует JSON-строку в список списков строк.
    @TypeConverter
    fun converterJsonToListOfList(string: String): List<List<String>> {
        return Gson().fromJson(
            string,
            object : TypeToken<List<List<String>>>() {}.type
        )
    }

    // Эта функция конвертирует список строк в JSON-строку.
    @TypeConverter
    fun converterListToJson(list: List<String>): String? {
        return Gson().toJson(list)
    }

    // Эта функция конвертирует JSON-строку в список строк.
    @TypeConverter
    fun converterJsonToList(string: String): List<String> {
        return Gson().fromJson(
            string,
            object : TypeToken<List<String>>() {}.type
        )
    }

    // Эта функция конвертирует время в формате Unix (тип Long) в объект класса Date.
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Эта функция конвертирует объект класса Date в время в формате Unix (тип Long).
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
