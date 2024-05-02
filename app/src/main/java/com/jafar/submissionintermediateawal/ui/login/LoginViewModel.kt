package com.jafar.submissionintermediateawal.ui.login

import androidx.lifecycle.ViewModel
import com.jafar.submissionintermediateawal.data.repository.AuthenticationRepository

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    suspend fun loginUser(
        email: String, password: String
    ) = authenticationRepository.loginUser(email, password)

}