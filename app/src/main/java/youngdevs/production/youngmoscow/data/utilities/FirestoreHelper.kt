package youngdevs.production.youngmoscow.data.utilities

import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import youngdevs.production.youngmoscow.data.entities.User

// Функция convertUserDocumentToEntity используется для конвертации объекта DocumentSnapshot из Firebase в объект User.
fun convertUserDocumentToEntity(id: String, document: DocumentSnapshot): User {
    val name: String = document.data?.get(UserDocumentProperties.name)!! as String
    val email: String = document.data?.get(UserDocumentProperties.email) as String
    return User(id, name, email )
}

// Функция конвертирует список списков строк в JSON-строку.
fun converterListOfListToJson(list: List<List<String?>>): String? {
    return Gson().toJson(list)
}

// Функция конвертирует JSON-строку в список списков строк.
fun converterJsonToListOfList(string: String?): List<List<String>> {
    return Gson().fromJson(
        string,
        object : TypeToken<List<List<String>>>() {}.type
    )
}
