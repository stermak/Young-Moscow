package youngdevs.production.youngmoscow.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
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

@Singleton
class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun createAccount(email: String, password: String, name: String): Boolean {
        var dbUser: User? = null
        var isSuccess = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser!!.uid
                    val newUser = hashMapOf(
                        "name" to name,
                        "email" to email,
                    )
                    isSuccess = true

                    dbUser = User(userId, name, email)
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

    override suspend fun authenticate(email: String, password: String): Boolean {

        var isSuccess = false
        val db = Firebase.firestore
        var id: String? = null

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
        db.collection(CollectionNames.users).document(auth.currentUser?.uid!!).get()
            .addOnSuccessListener {
                it?.let {
                    user = convertUserDocumentToEntity(id!!, it)
                }
            }
            .addOnFailureListener { Log.e(TAG, "FailureListener") }.await()

        withContext(Dispatchers.IO) {
            userDao.insert(user!!)
        }
        return isSuccess
    }

    override suspend fun getCurrentUser(): UserModel? {
        return userDao.getCurrentUser()?.asDomainModel()
    }

    override suspend fun updateUserProfile(userId: String, name: String, email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearUser() {
        withContext(Dispatchers.IO) {
            userDao.clear()
        }
    }


}