package com.jafar.submissionintermediateawal.ui.register

import androidx.lifecycle.ViewModel
import com.jafar.submissionintermediateawal.data.repository.AuthenticationRepository

class RegisterViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    suspend fun registerUser(
        name: String, email: String, password: String
    ) = authenticationRepository.registerUser(name, email, password)
}