package com.jafar.submissionintermediateawal.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jafar.submissionintermediateawal.databinding.ActivityLoginBinding
import com.jafar.submissionintermediateawal.ui.ViewModelFactory
import com.jafar.submissionintermediateawal.ui.home.HomeActivity
import com.jafar.submissionintermediateawal.ui.register.RegisterActivity
import com.jafar.submissionintermediateawal.utils.result.Result

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text?.trim().toString()
            val password = binding.edLoginPassword.text?.trim().toString()

            var isEmptyField = false

            if (email.isEmpty()) {
                binding.edLoginEmail.error = "Email harus diisi dulu"
                isEmptyField = true
            }

            if (password.isEmpty()) {
                binding.edLoginPassword.error = "Password harus diisi dulu"
                isEmptyField = true
            }

            if (!isEmptyField) {
                loginViewModel.finalLoginUser(email, password)
                loginViewModel.loginState.observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                showAlert(this, result.data.toString(), true)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                showAlert(this, result.error, false)
                                clearInput()
                            }
                        }
                    }
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            val intentToRegister = Intent(this, RegisterActivity::class.java)
            startActivity(intentToRegister)
        }
    }

    private fun showAlert(context: Context, message: String, isSuccess: Boolean) {
        val alert = AlertDialog.Builder(context)
        alert.setTitle("Login")
        alert.setMessage(message)
        alert.setPositiveButton("OK") { dialog, _ ->
            if (isSuccess)
                startActivity(Intent(context, HomeActivity::class.java))
            else
                dialog.dismiss()
        }
        val dialog = alert.create()
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun clearInput() {
        binding.edLoginPassword.setText("")
    }
}