package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.jdacodes.firebaseapp.core.Constants
import com.jdacodes.firebaseapp.feature_auth.data.repository.AuthRepositoryImpl
import com.jdacodes.firebaseapp.feature_auth.domain.repository.AuthRepository
import com.jdacodes.firebaseapp.feature_auth.domain.util.EmailValidator
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import javax.inject.Inject
@RunWith(AndroidJUnit4::class)
class ValidateEmailTest {
    // TODO: Events where not received when executing tests 
    private lateinit var validateEmail: ValidateEmail
    private lateinit var mockEmailValidator: EmailValidator

//    @Inject
//    lateinit var validateEmail: ValidateEmail
//
//    @Inject
//    @Mock
//    lateinit var mockEmailValidator: EmailValidator

//    @Inject
//    @Mock
//    lateinit var mockRepository: AuthRepository

    @Before
    fun setUp() {
        val mockAuth = Mockito.mock(FirebaseAuth::class.java)
        val mockRepository =
            Mockito.spy(AuthRepositoryImpl(mockAuth)) // Spy on the real implementation
        HiltAndroidRule(this).inject()
        mockEmailValidator = Mockito.mock(EmailValidator::class.java)
        validateEmail = ValidateEmail(mockRepository, mockEmailValidator)
    }

    @Test
    fun testValidEmail() {
        val validEmail = "valid@email.com"
        Mockito.`when`(mockEmailValidator.isValid(validEmail)).thenReturn(true)
//        Mockito.`when`(mockRepository.isValidEmail(validEmail))
//        Mockito.`when`(mockRepository.sendEmailVerification())
//            .thenReturn(true) // Stub the specific method

        val result = validateEmail.execute(validEmail)
        assertTrue(result.successful!!)
    }

    @Test
    fun testBlankEmail() {
        val email = ""
        val result = validateEmail.execute(email)

        assertFalse(result.successful!!)
        assertEquals(Constants.EMAIL_BLANK_ERROR_MESSAGE, result.errorMessage)
    }

}