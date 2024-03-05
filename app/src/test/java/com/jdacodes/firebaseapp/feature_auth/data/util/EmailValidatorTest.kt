package com.jdacodes.firebaseapp.feature_auth.data.util


import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EmailValidatorTest{

    private lateinit var emailValidatorImpl: EmailValidatorImpl
    @Test
    fun `invalid email returns false` () {
        emailValidatorImpl = EmailValidatorImpl()
        val email = "test.com"
        val result = emailValidatorImpl.isValid(email)
       assertThat(result).isFalse()

    }

    @Test
    fun `valid email returns true` () {
        emailValidatorImpl = EmailValidatorImpl()
        val email = "test@test.com"
        val result = emailValidatorImpl.isValid(email)
        assertThat(result).isTrue()

    }

    @Test
    fun `blank email returns false` () {
        emailValidatorImpl = EmailValidatorImpl()
        val email = ""
        val result = emailValidatorImpl.isValid(email)
        assertThat(result).isFalse()

    }
}