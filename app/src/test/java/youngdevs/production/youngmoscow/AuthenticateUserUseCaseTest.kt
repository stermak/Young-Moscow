package youngdevs.production.youngmoscow

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import youngdevs.production.youngmoscow.domain.models.UserModel
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCaseImpl

@RunWith(MockitoJUnitRunner::class)
class AuthenticateUserUseCaseTest {

    // Создание mock-объектов для UserRepository и AuthenticateUserUseCase
    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var authenticateUserUseCase: AuthenticateUserUseCase

    @Before
    fun setUp() {
        authenticateUserUseCase = AuthenticateUserUseCaseImpl(userRepository)
    }

    @Test
    fun testSignIn() = runBlocking {
        // Подготовка данных для теста
        val email = "test@example.com"
        val password = "password"

        // Замокать поведение
        `when`(userRepository.authenticate(email, password)).thenReturn(true)

        // Вызов тестируемого метода
        val result = authenticateUserUseCase.signIn(email, password)

        // Проверка результата
        assertTrue(result)
    }

    @Test
    fun testGetCurrentUser() = runBlocking {
        // Замокать поведение
        val mockUser = UserModel(id = "1") // замените это на настоящий объект UserModel
        `when`(userRepository.getCurrentUser()).thenReturn(mockUser)

        // Вызов тестируемого метода
        val result = authenticateUserUseCase.getCurrentUser()

        // Проверка результата
        assertNotNull(result)
    }

    @Test
    fun testSignOut() = runBlocking {
        // Замокать поведение не нужно, так как метод signOut не возвращает результат

        // Вызов тестируемого метода
        authenticateUserUseCase.signOut()

        // Дополнительные проверки (если необходимо)
        // ...
    }

    @Test
    fun testSignInWithGoogle() = runBlocking {
        // Подготовка данных для теста
        val idToken = "google_id_token"

        // Замокать поведение
        `when`(userRepository.authenticateWithGoogle(idToken)).thenReturn(true)

        // Вызов тестируемого метода
        val result = authenticateUserUseCase.signInWithGoogle(idToken)

        // Проверка результата
        assertTrue(result)
    }

    @Test
    fun testCreateAccount() = runBlocking {
        // Подготовка данных для теста
        val email = "test@example.com"
        val password = "password"
        val repeatPassword = "password"
        val name = "John Doe"

        // Замокать поведение
        `when`(userRepository.createAccount(email, password, name, null)).thenReturn(true)

        // Вызов тестируемого метода
        val result = authenticateUserUseCase.createAccount(email, password, repeatPassword, name)

        // Проверка результата
        assertEquals(1, result)
    }

    @Test
    fun testCheckAccountExists() = runBlocking {
        // Подготовка данных для теста
        val email = "test@example.com"

        // Замокать поведение
        `when`(userRepository.checkAccountExists(email)).thenReturn(true)

        // Вызов тестируемого метода
        val result = authenticateUserUseCase.checkAccountExists(email)

        // Проверка результата
        assertTrue(result)
    }
}
