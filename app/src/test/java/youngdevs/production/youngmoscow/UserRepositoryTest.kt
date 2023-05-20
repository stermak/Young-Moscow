package youngdevs.production.youngmoscow

import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.repository.UserRepositoryImpl

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {
    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    // Добавлены моки для Firestore
    @Mock
    private lateinit var firebaseFirestore: FirebaseFirestore
    @Mock
    private lateinit var firebaseFirestoreCollection: CollectionReference
    @Mock
    private lateinit var firebaseFirestoreDocument: DocumentReference
    @Mock
    private lateinit var writeBatch: WriteBatch

    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userRepository = UserRepositoryImpl(firebaseAuth)
        `when`(firebaseFirestore.collection(any())).thenReturn(firebaseFirestoreCollection)
        `when`(firebaseFirestoreCollection.document(any())).thenReturn(firebaseFirestoreDocument)
        `when`(firebaseFirestore.batch()).thenReturn(writeBatch)
    }


    @Test
    fun testCreateAccount_Success() {
        runBlocking {
            val taskCompletionSource = TaskCompletionSource<AuthResult>()
            val task = taskCompletionSource.task
            taskCompletionSource.setResult(Mockito.mock(AuthResult::class.java))

            whenever(firebaseAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(task)

            val result = userRepository.createAccount("test@example.com", "password", "John Doe")
            assertTrue(result)

            verify(firebaseAuth).createUserWithEmailAndPassword("test@example.com", "password")
        }
    }

    @Test
    fun testCreateAccount_Failure() {
        runBlocking {
            val taskCompletionSource = TaskCompletionSource<AuthResult>()
            val task = taskCompletionSource.task
            taskCompletionSource.setException(Exception())

            whenever(firebaseAuth.createUserWithEmailAndPassword(any(), any())).thenReturn(task)

            val result = userRepository.createAccount("test@example.com", "password", "John Doe")
            assertFalse(result)

            verify(firebaseAuth).createUserWithEmailAndPassword("test@example.com", "password")
        }
    }


    @Test
    fun testAuthenticate_Success() = runBlocking {
        val taskCompletionSource = TaskCompletionSource<AuthResult>()
        val task = taskCompletionSource.task
        taskCompletionSource.setResult(Mockito.mock(AuthResult::class.java))

        Mockito.`when`(firebaseAuth.signInWithEmailAndPassword(any(), any())).thenReturn(task)

        val result = userRepository.authenticate("test@example.com", "password")
        assertTrue(result)

        verify(firebaseAuth).signInWithEmailAndPassword("test@example.com", "password")
    }

    @Test
    fun testAuthenticate_Failure() = runBlocking {
        val taskCompletionSource = TaskCompletionSource<AuthResult>()
        val task = taskCompletionSource.task
        taskCompletionSource.setException(Exception())

        Mockito.`when`(firebaseAuth.signInWithEmailAndPassword(any(), any())).thenReturn(task)

        val result = userRepository.authenticate("test@example.com", "password")
        assertFalse(result)

        verify(firebaseAuth).signInWithEmailAndPassword("test@example.com", "password")
    }

    @Test
    fun testGetCurrentUser_Success() = runBlocking {
        val firebaseUser = Mockito.mock(FirebaseUser::class.java)
        val documentSnapshot = Mockito.mock(DocumentSnapshot::class.java)

        Mockito.`when`(firebaseAuth.currentUser).thenReturn(firebaseUser)
        Mockito.`when`(firebaseUser.uid).thenReturn("test_uid")
        Mockito.`when`(documentSnapshot.getString(any())).thenReturn("test_value")

        val taskCompletionSource = TaskCompletionSource<DocumentSnapshot>()
        val task = taskCompletionSource.task
        taskCompletionSource.setResult(documentSnapshot)

        Mockito.`when`(firebaseFirestore.collection(any()).document(any()).get()).thenReturn(task)

        val result = userRepository.getCurrentUser()
        assertNotNull(result)

        assertEquals(result?.id, "test_uid")
        assertEquals(result?.name, "test_value")
        assertEquals(result?.email, "test_value")
        assertEquals(result?.phone, "test_value")
    }

    @Test
    fun testUpdateUserProfile_Success() = runBlocking {
        val writeBatch = Mockito.mock(WriteBatch::class.java)

        `when`(firebaseFirestore.batch()).thenReturn(writeBatch)
        `when`(writeBatch.commit()).thenReturn(Tasks.forResult(null))

        userRepository.updateUserProfile("test_uid", "test_name", "test_email", "test_phone")

        verify(firebaseFirestore.collection(any()).document(any())).update(anyMap())
    }


}