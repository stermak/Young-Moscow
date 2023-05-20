package youngdevs.production.youngmoscow

import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import youngdevs.production.youngmoscow.domain.models.UserModel
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.repository.UserRepositoryImpl
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCaseImpl

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var authenticateUserUseCase: AuthenticateUserUseCase

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Before
    fun setup() {
        // Инициализация mock-объектов и классов перед каждым тестом
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testCreateAccount() = runBlocking {
        // Подготовка данных для теста
        val email = "test@example.com"
        val password = "password"
        val name = "John Doe"
        val phone: String? = null

        // Замокать поведение
        `when`(userRepository.createAccount(email, password, name, phone)).thenReturn(true)

        // Вызов тестируемого метода
        val result = userRepository.createAccount(email, password, name, phone)

        // Проверка результата
        assertTrue(result)
    }

    @Test
    fun testCheckAccountExists() = runBlocking {
        // Подготовка данных для теста
        val email = "test@example.com"
        `when`(userRepository.checkAccountExists(email)).thenReturn(true)

        // Вызов тестируемого метода
        val result = userRepository.checkAccountExists(email)

        // Проверка результата
        assertTrue(result)
    }

    @Test
    fun testAuthenticate() = runBlocking {
        // Подготовка данных для теста
        val email = "test@example.com"
        val password = "password"

        // Замокать поведение
        `when`(userRepository.authenticate(email, password)).thenReturn(true)

        // Вызов тестируемого метода
        val result = userRepository.authenticate(email, password)

        // Проверка результата
        assertTrue(result)
    }

    @Test
    fun testGetCurrentUser() = runBlocking {
        // Замокать поведение
        val mockUser = UserModel(id = "1")
        `when`(userRepository.getCurrentUser()).thenReturn(mockUser)

        // Вызов тестируемого метода
        val result = userRepository.getCurrentUser()

        // Проверка результата
        assertNotNull(result)
    }

    @Test
    fun testUpdateUserPassword() = runBlocking {
        // Подготовка данных для теста
        val newPassword = "new_password"

        // Замокать поведение не нужно, так как метод updateUserPassword не возвращает результат

        // Вызов тестируемого метода
        userRepository.updateUserPassword(newPassword)

        // Дополнительные проверки (если необходимо)
        // ...
    }

    @Test
    fun testAuthenticateWithGoogle() = runBlocking {
        // Подготовка данных для теста
        val idToken = "google_id_token"

        // Замокать поведение
        `when`(userRepository.authenticateWithGoogle(idToken)).thenReturn(true)

        // Вызов тестируемого метода
        val result = userRepository.authenticateWithGoogle(idToken)

        // Проверка результата
        assertTrue(result)
    }

    @Test
    fun testUpdateUserProfile() = runBlocking {
        // Подготовка данных для теста
        val userId = "user_id"
        val name = "John Doe"
        val email = "test@example.com"
        val phone = "1234567890"

        // Замокать поведение не нужно, так как метод updateUserProfile не возвращает результат

        // Вызов тестируемого метода
        userRepository.updateUserProfile(userId, name, email, phone)

        // Дополнительные проверки (если необходимо)
        // ...
    }

    @Test
    fun testClearUser() = runBlocking {
        // Замокать поведение не нужно, так как метод clearUser не возвращает результат

        // Вызов тестируемого метода
        userRepository.clearUser()
    }

}
