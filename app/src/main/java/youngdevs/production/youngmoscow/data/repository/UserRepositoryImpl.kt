package youngdevs.production.youngmoscow.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import youngdevs.production.youngmoscow.data.dao.UserDao
import youngdevs.production.youngmoscow.data.entities.User
import youngdevs.production.youngmoscow.data.entities.asDomainModel
import youngdevs.production.youngmoscow.data.utilities.CollectionNames
import youngdevs.production.youngmoscow.data.utilities.convertUserDocumentToEntity
import youngdevs.production.youngmoscow.domain.models.UserModel
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

// Этот класс является реализацией UserRepository и используется для работы с пользователями в Firebase.
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth
) : UserRepository {

    // Создаем экземпляр класса FirebaseAuth для аутентификации пользователя.
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Функция createAccount используется для создания нового аккаунта пользователя в Firebase.
    override suspend fun createAccount(
        email: String,
        password: String,
        name: String,
        phone: String?
    ): Boolean {
        var dbUser: User? = null
        var isSuccess = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser!!.uid

                    val newUser = hashMapOf<String, Any>(
                        "name" to name,
                        "email" to email
                    )

                    // Добавляем телефон только если он не равен null
                    phone?.let {
                        newUser["phone"] = it
                    }

                    isSuccess = true

                    dbUser = User(userId, name, email, phone ?: "")
                    val db = Firebase.firestore
                    db.collection(CollectionNames.users).document(userId)
                        .set(newUser)
                        .addOnSuccessListener {
                            isSuccess = true
                        }
                        .addOnFailureListener { Log.e(TAG, "Error writing document") }
                }
            }.addOnFailureListener { Exception -> Log.e(TAG, Exception.toString()) }
            .await()

        if (isSuccess) {
            withContext(Dispatchers.IO) {
                userDao.insert(dbUser!!)
            }
        }
        return isSuccess
    }


    // Функция authenticate используется для аутентификации пользователя в Firebase.
    override suspend fun authenticate(email: String, password: String): Boolean {

        var isSuccess = false
        val db = Firebase.firestore
        var id: String? = null

        // Аутентифицируем пользователя с помощью email и password в Firebase.
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    isSuccess = true
                    id = auth.currentUser?.uid!!
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                }
            }
            .addOnFailureListener { Log.e(TAG, "FailureListener") }.await()

        var user: User? = null

        // Получаем информацию о пользователе из Firestore и конвертируем ее в объект User.
        db.collection(CollectionNames.users).document(auth.currentUser?.uid!!).get()
            .addOnSuccessListener {
                it?.let {
                    user = convertUserDocumentToEntity(id!!, it)
                }
            }
            .addOnFailureListener { Log.e(TAG, "FailureListener") }.await()

        // Добавляем пользователя в локальную базу данных.
        withContext(Dispatchers.IO) {
            userDao.insert(user!!)
        }
        return isSuccess
    }

    // Функция getCurrentUser возвращает текущего пользователя из локальной базы данных.
    override suspend fun getCurrentUser(): UserModel? {
        return userDao.getCurrentUser()?.asDomainModel()
    }

    override suspend fun authenticateWithGoogle(idToken: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(credential).await()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    // Функция updateUserProfile используется для обновления информации о пользователе в Firebase.
    override suspend fun updateUserProfile(
        userId: String,
        name: String,
        email: String,
        phone: String
    ) {
        // Обновление данных пользователя в Firebase и локальной базе данных
        val userDataToUpdate = hashMapOf<String, Any>(
            "name" to name,
            "email" to email,
            "phone" to phone
        )

        val db = Firebase.firestore
        db.collection(CollectionNames.users).document(userId)
            .update(userDataToUpdate)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            .await()

        withContext(Dispatchers.IO) {
            userDao.updateUser(userId, name, email, phone)
        }
    }

    override suspend fun updateUserPassword(newPassword: String) {
        auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User password updated.")
            } else {
                Log.w(TAG, "Error updating user password", task.exception)
            }
        }?.addOnFailureListener { Log.e(TAG, "FailureListener") }?.await()
    }


    // Функция clearUser очищает локальную базу данных от всех пользователей.
    override suspend fun clearUser() {
        withContext(Dispatchers.IO) {
            userDao.clear()
        }
    }
}
