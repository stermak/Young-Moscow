package youngdevs.production.youngmoscow

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCase
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCaseImpl

@RunWith(MockitoJUnitRunner::class)
class AuthenticationTest {

    // Создание mock-объектов для UserRepository и AuthenticateUserUseCase
    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var authenticateUserUseCase: AuthenticateUserUseCase

    @Before
    fun setup() {
        // Инициализация AuthenticateUserUseCaseImpl с использованием mock-объекта UserRepository
        authenticateUserUseCase = AuthenticateUserUseCaseImpl(userRepository)
    }

    @Test
    suspend fun testSignIn_Success() {
        // Заглушка для userRepository.authenticate
        runBlocking {
            whenever(userRepository.authenticate(any(), any())).thenReturn(true)
        }

        // Вызов метода signIn в AuthenticateUserUseCaseImpl
        runBlocking {
            val result = authenticateUserUseCase.signIn("test@example.com", "password")
            assertTrue(result)
        }

        // Проверка, что метод userRepository.authenticate был вызван с правильными аргументами
        verify(userRepository).authenticate("test@example.com", "password")
    }

    @Test
    suspend fun testSignIn_Failure() {
        // Заглушка для userRepository.authenticate
        runBlocking {
            whenever(userRepository.authenticate(any(), any())).thenReturn(false)
        }

        // Вызов метода signIn в AuthenticateUserUseCaseImpl
        runBlocking {
            val result = authenticateUserUseCase.signIn("test@example.com", "password")
            assertFalse(result)
        }

        // Проверка, что метод userRepository.authenticate был вызван с правильными аргументами
        verify(userRepository).authenticate("test@example.com", "password")
    }

    // Добавьте другие тесты для остальных методов AuthenticateUserUseCaseImpl

}
