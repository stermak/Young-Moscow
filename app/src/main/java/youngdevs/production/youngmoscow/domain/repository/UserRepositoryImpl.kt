package youngdevs.production.youngmoscow.domain.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
        val signInMethods = firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods
        return signInMethods != null && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)
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
                    phone = userDocument["phone"] as? String,
                    profileImage = userDocument["profileImage"] as? String // Получите URL профильного изображения из Firestore
                )

            }
        }
    }

    override suspend fun createAccountWithGoogle(email: String, name: String): Boolean = coroutineScope {
        var isSuccess = false

        try {
            val newUser = hashMapOf<String, Any>(
                "name" to name,
                "email" to email
            )

            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            Firebase.firestore.collection("users")
                .document(userId)
                .set(newUser)
                .await()

            isSuccess = true
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", e.toString())
        }

        isSuccess
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

    override suspend fun reauthenticate(email: String, password: String): Boolean = coroutineScope {
        var isSuccess = false
        try {
            val user = auth.currentUser
            val credential = EmailAuthProvider.getCredential(email, password)
            user?.reauthenticate(credential)?.await()
            isSuccess = true
        } catch (e: Exception) {
            Log.e(TAG, "Reauthentication failed", e)
        }
        isSuccess
    }
    override suspend fun clearUser() {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPassword(newPassword: String) {
        val user = firebaseAuth.currentUser
        user?.let {
            it.updatePassword(newPassword).await()
            Log.d(TAG, "User password updated.")
        } ?: Log.e(TAG, "No authenticated user.")
    }


    suspend fun uploadProfileImage(imageUri: Uri): String = withContext(Dispatchers.IO) {
        val userId =
            FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("Пользователь не найден")
        val storageRef = Firebase.storage.reference.child("profile_images/$userId")
        val uploadTask = storageRef.putFile(imageUri)

        // Await the upload task to complete
        uploadTask.await()

        // Get the downloadable URL for the uploaded image file
        storageRef.downloadUrl.await().toString()
    }

    suspend fun updateProfileImage(imageUrl: String) = withContext(Dispatchers.IO) {
        val userId =
            FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("Пользователь не найден")
        val databaseRef = Firebase.firestore.collection(CollectionNames.users).document(userId)

        // Update the profileImage field of the Firestore document
        databaseRef.update("profileImage", imageUrl).await()
    }


}