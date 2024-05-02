package com.jafar.submissionintermediateawal.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jafar.submissionintermediateawal.databinding.ActivityRegisterBinding
import com.jafar.submissionintermediateawal.ui.ViewModelFactory
import com.jafar.submissionintermediateawal.ui.login.LoginActivity
import com.jafar.submissionintermediateawal.utils.result.Result
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            name = binding.edRegisterNama.text.toString().trim()
            email = binding.edRegisterEmail.text.toString().trim()
            password = binding.edRegisterPassword.text.toString().trim()

            var isEmptyField = false
            if (name.isEmpty()) {
                binding.edRegisterNama.error = "Nama harus diisi dulu"
                isEmptyField = true
            }

            if (email.isEmpty()) {
                binding.edRegisterEmail.error = "Email harus diisi dulu"
                isEmptyField = true
            }

            if (password.isEmpty()) {
                binding.edRegisterPassword.error = "Password harus diisi dulu"
                isEmptyField = true
            }

            if (!isEmptyField) {
                lifecycleScope.launch {
                    registerViewModel.registerUser(name, email, password).observe(this@RegisterActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showLoading(false)
                                    showAlert(this@RegisterActivity, result.data.toString(),true)
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    showAlert(this@RegisterActivity, result.error, false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showAlert(context: Context, message: String, isSuccess: Boolean) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(
            if (isSuccess) "Yeay :)" else "Yahh :("
        )
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            if (isSuccess) {
                val intentToLogin = Intent(context, LoginActivity::class.java)
                startActivity(intentToLogin)
            } else {
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}