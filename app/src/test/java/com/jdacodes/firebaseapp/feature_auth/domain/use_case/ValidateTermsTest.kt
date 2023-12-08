package com.jdacodes.firebaseapp.feature_auth.domain.use_case

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ValidateTermsTest {
    private lateinit var validateTerms: ValidateTerms

    @Before
    fun setUp() {
        validateTerms = ValidateTerms()
    }

    @Test
    fun `Terms not accepted, returns error`() {
        val result = validateTerms.execute(false)
        assertFalse(result.successful!!)
    }
}