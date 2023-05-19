package youngdevs.production.youngmoscow.domain.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import youngdevs.production.youngmoscow.data.utilities.CollectionNames
import youngdevs.production.youngmoscow.domain.models.UserModel
import javax.inject.Inject
import javax.inject.Singleton

// Этот класс является реализацией UserRepository и используется для работы с пользователями в
// Firebase.
@Singleton
class UserRepositoryImpl
@Inject
constructor(
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
    ): Boolean = coroutineScope {
        var isSuccess = false

        try {
            auth.createUserWithEmailAndPassword(email, password).await()

            val newUser = hashMapOf<String, Any>(
                "name" to name,
                "email" to email
            )

            phone?.let { newUser["phone"] = it }

            val userId = auth.currentUser!!.uid
            Firebase.firestore.collection(CollectionNames.users)
                .document(userId)
                .set(newUser)
                .await()

            isSuccess = true
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

        isSuccess
    }


    override suspend fun checkAccountExists(email: String): Boolean {
        val auth = FirebaseAuth.getInstance()
        val signInMethods = auth.fetchSignInMethodsForEmail(email).await().signInMethods
        return signInMethods != null && signInMethods.isNotEmpty()
    }


    // Функция authenticate используется для аутентификации пользователя в Firebase.
    override suspend fun authenticate(
        email: String,
        password: String
    ): Boolean = coroutineScope {
        var isSuccess = false

        try {
            auth.signInWithEmailAndPassword(email, password).await()
            isSuccess = true
        } catch (e: Exception) {
            Log.e(TAG, "FailureListener", e)
        }

        isSuccess
    }

    override suspend fun getCurrentUser(): UserModel? {
        return withContext(Dispatchers.IO) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let { firebaseUser ->
                val userDocument = Firebase.firestore.collection(CollectionNames.users)
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                UserModel(
                    id = firebaseUser.uid,
                    name = userDocument["name"] as? String ?: "",
                    email = userDocument["email"] as? String,
                    phone = userDocument["phone"] as? String
                    // Добавьте остальные поля здесь, если они вам нужны
                )
            }
        }
    }




    override suspend fun authenticateWithGoogle(idToken: String): Boolean {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }


    // Функция updateUserProfile используется для обновления информации о пользователе в Firebase.
    override suspend fun updateUserProfile(
        userId: String,
        name: String,
        email: String,
        phone: String
    ) {

        val userDataToUpdate =
            hashMapOf<String, Any>(
                "name" to name,
                "email" to email,
                "phone" to phone
            )

        val db = Firebase.firestore
        db.collection(CollectionNames.users)
            .document(userId)
            .update(userDataToUpdate)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
            }
            .await()
    }

    override suspend fun clearUser() {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPassword(newPassword: String) {
        auth.currentUser
            ?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                } else {
                    Log.w(
                        TAG,
                        "Error updating user password",
                        task.exception
                    )
                }
            }
            ?.addOnFailureListener { Log.e(TAG, "FailureListener") }
            ?.await()
    }



    suspend fun uploadProfileImage(imageUri: Uri): String = withContext(Dispatchers.IO) {
        val userId =
            FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("Пользователь не найден")
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$userId")
        val uploadTask = storageRef.putFile(imageUri)

        val snapshot = uploadTask.await()
        val downloadUrl = snapshot.metadata?.reference?.downloadUrl
        downloadUrl?.await().toString()
    }

    suspend fun updateProfileImage(imageUrl: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("Пользователь не найден")
        val databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)
        databaseRef.child("profileImage").setValue(imageUrl)
            .addOnSuccessListener { Log.d(TAG, "Профильное изображение успешно обновлено") }
            .addOnFailureListener { e -> Log.w(TAG, "Ошибка при обновлении профильного изображения", e) }
            .await()
    }

}
