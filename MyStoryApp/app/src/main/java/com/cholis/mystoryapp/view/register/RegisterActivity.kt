package com.cholis.mystoryapp.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cholis.mystoryapp.R
import com.cholis.mystoryapp.Result
import com.cholis.mystoryapp.view.FactoryVM
import com.cholis.mystoryapp.view.customview.CustomViewEditText
import com.cholis.mystoryapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerViewModel: RegisterVM
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        registerViewModel = ViewModelProvider(this, FactoryVM(this))[RegisterVM::class.java]

        val nameEditText: CustomViewEditText = findViewById(R.id.nameEditText)
        val emailEditText: CustomViewEditText = findViewById(R.id.emailEditText)
        val passwordEditText: CustomViewEditText = findViewById(R.id.passwordEditText)
        val registerButton: Button = findViewById(R.id.registerButton)
        val loginButton: Button = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)

        // Set a click listener for the register button
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            register(name, email, password)
        }

        // Set a click listener for the login button
        loginButton.setOnClickListener {
            // Redirect to LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    private fun register(name: String, email: String, password: String) {
        val registerResult = registerViewModel.register(name, email, password)

        registerResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> showProgress()
                is Result.Success -> {
                    hideProgress()
                    Toast.makeText(this, "Registrasi berhasil silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                    // Redirect to MainActivity after successful registration
                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                    finish()
                }

                is Result.Error -> {
                    hideProgress()
                    val errorMessage = result.error
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}
