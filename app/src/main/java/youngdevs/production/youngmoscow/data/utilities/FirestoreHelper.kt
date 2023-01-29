package youngdevs.production.youngmoscow.data.utilities

import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import youngdevs.production.youngmoscow.data.entities.User

fun convertUserDocumentToEntity(id: String, document: DocumentSnapshot): User {
    val name: String = document.data?.get(UserDocumentProperties.name)!! as String
    val email: String = document.data?.get(UserDocumentProperties.email) as String
    return User(id, name, email )
}

fun converterListOfListToJson(list: List<List<String?>>): String? {
    return Gson().toJson(list)
}


fun converterJsonToListOfList(string: String?): List<List<String>> {
    return Gson().fromJson(
        string,
        object : TypeToken<List<List<String>>>() {}.type
    )
}
