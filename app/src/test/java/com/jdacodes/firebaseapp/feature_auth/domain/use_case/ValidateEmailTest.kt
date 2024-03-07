package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import com.jdacodes.firebaseapp.core.Constants
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.domain.util.EmailValidator
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class ValidateEmailTest {
    private lateinit var validateEmail: ValidateEmail

    @MockK
    private lateinit var mockEmailValidator: EmailValidator

    @MockK
    private lateinit var mockRepository: AuthRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        validateEmail = produceUseCase()
    }

    private fun produceUseCase() = ValidateEmail(
        repo = mockRepository,
        emailValidator = mockEmailValidator

    )
@Test
fun `empty email returns false and error message`() = runBlocking {
    val email = ""

    val result = validateEmail.execute(email)

    assert(result.successful == false)
    assert(result.errorMessage == Constants.EMAIL_BLANK_ERROR_MESSAGE)
    verify { mockEmailValidator wasNot Called }
}

    @Test
    fun `invalid email returns false and error message`()  = runBlocking {
        val email = "invalidEmail"
        coEvery { mockEmailValidator.isValid(email) }.returns(false)

        val result = validateEmail.execute(email)

        assert(result.successful == false)
        assert(result.errorMessage == Constants.EMAIL_INVALID_ERROR_MESSAGE)
        verify { mockEmailValidator.isValid(email) }
    }

    @Test
    fun `valid email returns true and no error message`() = runBlocking {
        val email = "valid@email.com"
        coEvery { mockEmailValidator.isValid(email) }.returns(true)

        val result = validateEmail.execute(email)

        assert(result.successful == true)
        assert(result.errorMessage == null)
        verify { mockEmailValidator.isValid(email) }
    }



}