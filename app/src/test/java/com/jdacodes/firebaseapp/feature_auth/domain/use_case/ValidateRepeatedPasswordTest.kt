package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ValidateRepeatedPasswordTest {
    private lateinit var validateRepeatedPassword: ValidateRepeatedPassword
    @Before
    fun setUp() {
       validateRepeatedPassword = ValidateRepeatedPassword()
    }

    @Test
    fun `Password does not match, returns error`() {
        val result = validateRepeatedPassword.execute(
            password = "test1234",
            repeatedPassword = "test5678"
        )
        assertEquals(false, result.successful)
    }
}