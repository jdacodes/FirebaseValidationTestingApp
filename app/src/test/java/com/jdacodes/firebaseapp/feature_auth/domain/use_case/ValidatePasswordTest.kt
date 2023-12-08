package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ValidatePasswordTest {
    private lateinit var validatePassword: ValidatePassword
    @Before
    fun setUp() {
        validatePassword = ValidatePassword()
    }

    @Test
    fun `Password is 8 or more, returns error`() {
        val result = validatePassword.execute("test123")
        assertEquals(false, result.successful)
    }
    @Test
    fun `Password is letter-only, returns error`() {
        val result = validatePassword.execute("abcdefgh")
        assertEquals(false, result.successful)
    }
    @Test
    fun `Password is digit-only, returns error`() {
        val result = validatePassword.execute("12345678")
        assertEquals(false, result.successful)
    }
}