package youngdevs.production.youngmoscow

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import youngdevs.production.youngmoscow.domain.models.UserModel
import youngdevs.production.youngmoscow.domain.repository.UserRepository
import youngdevs.production.youngmoscow.domain.usecases.AuthenticateUserUseCaseImpl

class AuthenticateUserUseCaseImplTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var authenticateUserUseCaseImpl: AuthenticateUserUseCaseImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        authenticateUserUseCaseImpl = AuthenticateUserUseCaseImpl(userRepository)
    }

    @Test
    fun signIn_withEmptyCredentials_returnsFalse() = runBlocking {
        val result = authenticateUserUseCaseImpl.signIn("", "")
        assertEquals(false, result)
    }
    @Test
    fun signIn_withCorrectCredentials_returnsTrue() = runBlocking {
        // Мокирование зависимости userRepository
        `when`(userRepository.authenticate("email@example.com", "password123")).thenReturn(true)

        val result = authenticateUserUseCaseImpl.signIn("email@example.com", "password123")
        assertEquals(true, result)
    }

    @Test
    fun signIn_withIncorrectCredentials_returnsFalse() = runBlocking {
        // Мокирование зависимости userRepository
        `when`(userRepository.authenticate("email@example.com", "password123")).thenReturn(false)

        val result = authenticateUserUseCaseImpl.signIn("email@example.com", "password123")
        assertEquals(false, result)
    }

    @Test
    fun getCurrentUser_whenUserIsAuthenticated_returnsUserModel() = runBlocking {
        // Мокирование зависимости userRepository
        val userModel = UserModel("123", "John Doe", "john@example.com")
        `when`(userRepository.getCurrentUser()).thenReturn(userModel)

        val result = authenticateUserUseCaseImpl.getCurrentUser()
        assertEquals(userModel, result)
    }
    @Test
    fun signOut_callsClearUserInUserRepository() = runBlocking {
        // Мокирование зависимости userRepository
        authenticateUserUseCaseImpl.signOut()

        // Проверка, что метод clearUser() был вызван в userRepository
        verify(userRepository).clearUser()
    }

    @Test
    fun createAccount_withMatchingPasswords_callsCreateAccountInUserRepository() = runBlocking {
        // Мокирование зависимости userRepository
        `when`(
            userRepository.createAccount(
                "email@example.com",
                "password123",
                "password123",
                "John Doe"
            )
        ).thenReturn(true)

        val result = authenticateUserUseCaseImpl.createAccount(
            "email@example.com",
            "password123",
            "password123",
            "John Doe"
        )

        // Проверка, что метод createAccount() был вызван в userRepository и возвращает ожидаемый результат
        verify(userRepository).createAccount("email@example.com", "password123", "John Doe")
        assertEquals(1, result)
    }

    @Test
    fun createAccount_withNonMatchingPasswords_returnsFailure() = runBlocking {
        val result = authenticateUserUseCaseImpl.createAccount(
            "email@example.com",
            "password123",
            "password456",
            "John Doe"
        )

        // Проверка, что метод createAccount() не вызывался в userRepository и возвращается ожидаемый результат
        verifyNoMoreInteractions(userRepository)
        assertEquals(-1, result)
    }

    @Test
    fun checkAccountExists_withExistingEmail_returnsTrue() = runBlocking {
        // Мокирование зависимости userRepository
        `when`(userRepository.checkAccountExists("email@example.com")).thenReturn(true)

        val result = authenticateUserUseCaseImpl.checkAccountExists("email@example.com")

        // Проверка, что метод checkAccountExists() был вызван в userRepository и возвращает ожидаемый результат
        verify(userRepository).checkAccountExists("email@example.com")
        assertEquals(true, result)
    }

    @Test
    fun checkAccountExists_withNonExistingEmail_returnsFalse() = runBlocking {
        // Мокирование зависимости userRepository
        `when`(userRepository.checkAccountExists("email@example.com")).thenReturn(false)

        val result = authenticateUserUseCaseImpl.checkAccountExists("email@example.com")

        // Проверка, что метод checkAccountExists() был вызван в userRepository и возвращает ожидаемый результат
        verify(userRepository).checkAccountExists("email@example.com")
        assertEquals(false, result)
    }
    @Test
    fun signIn_withValidCredentials_returnsTrue() = runBlocking {
        // Мокирование зависимости userRepository
        `when`(userRepository.authenticate("email@example.com", "password123")).thenReturn(true)

        val result = authenticateUserUseCaseImpl.signIn("email@example.com", "password123")

        // Проверка, что метод authenticate() был вызван в userRepository и возвращает ожидаемый результат
        verify(userRepository).authenticate("email@example.com", "password123")
        assertEquals(true, result)
    }

    @Test
    fun signIn_withEmptyEmail_returnsFalse() = runBlocking {
        val result = authenticateUserUseCaseImpl.signIn("", "password123")

        // Проверка, что метод authenticate() не вызывался в userRepository и возвращается ожидаемый результат
        verifyNoMoreInteractions(userRepository)
        assertEquals(false, result)
    }

    @Test
    fun signIn_withEmptyPassword_returnsFalse() = runBlocking {
        val result = authenticateUserUseCaseImpl.signIn("email@example.com", "")

        // Проверка, что метод authenticate() не вызывался в userRepository и возвращается ожидаемый результат
        verifyNoMoreInteractions(userRepository)
        assertEquals(false, result)
    }

    @Test
    fun getCurrentUser_callsGetCurrentUserInUserRepository() = runBlocking {
        // Мокирование зависимости userRepository
        `when`(userRepository.getCurrentUser()).thenReturn(UserModel("123", "John Doe", "email@example.com"))

        val result = authenticateUserUseCaseImpl.getCurrentUser()

        // Проверка, что метод getCurrentUser() был вызван в userRepository и возвращает ожидаемый результат
        verify(userRepository).getCurrentUser()
        assertEquals(UserModel("123", "John Doe", "email@example.com"), result)
    }

    @Test
    fun signInWithGoogle_withValidToken_returnsTrue() = runBlocking {
        // Мокирование зависимости userRepository
        `when`(userRepository.authenticateWithGoogle("google_token")).thenReturn(true)

        val result = authenticateUserUseCaseImpl.signInWithGoogle("google_token")

        // Проверка, что метод authenticateWithGoogle() был вызван в userRepository и возвращает ожидаемый результат
        verify(userRepository).authenticateWithGoogle("google_token")
        assertEquals(true, result)
    }
}