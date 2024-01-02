package com.cholis.mystoryapp.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cholis.mystoryapp.R
import com.cholis.mystoryapp.preferences.Preferences
import com.cholis.mystoryapp.Result
import com.cholis.mystoryapp.view.FactoryVM
import com.cholis.mystoryapp.view.customview.CustomViewEditText
import com.cholis.mystoryapp.view.liststory.ListStoryActivity
import com.cholis.mystoryapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginVM
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        // Inisialisasi ViewModel untuk aktivitas login
        loginViewModel = ViewModelProvider(this, FactoryVM(this))[LoginVM::class.java]

        // Menghubungkan elemen-elemen UI dengan variabel
        val emailEditText: CustomViewEditText = findViewById(R.id.emailEditText)
        val passwordEditText: CustomViewEditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.registerButton)
        progressBar = findViewById(R.id.progressBar)

        // Menambahkan listener untuk tombol login
        loginButton.setOnClickListener {
            login(emailEditText.text.toString(), passwordEditText.text.toString())
        }

        // Menambahkan listener untuk tombol register
        registerButton.setOnClickListener { openRegisterActivity() }
    }

    private fun login(email: String, password: String) {
        // Memanggil fungsi loginViewModel.login dan mendapatkan hasilnya
        val loginResult = loginViewModel.login(email, password)

        // Mengamati hasil dari login
        loginResult.observe(this) { result ->
            when (result) {
                // Jika sedang dalam proses loading
                is Result.Loading -> showProgress()
                // Jika login berhasil
                is Result.Success -> {
                    hideProgress()
                    // Menyimpan token hasil login (jika diperlukan)
                    Preferences.saveToken(result.data.loginResult.token, this@LoginActivity)
                    // Navigasi ke aktivitas utama (MainActivity)
                    val mainActivityIntent = Intent(this, ListStoryActivity::class.java)
                    startActivity(mainActivityIntent)
                    finish() // Menutup aktivitas login
                }

                // Jika terjadi kesalahan saat login
                is Result.Error -> {
                    hideProgress()
                    val errorMessage = result.error
                    // Menampilkan pesan kesalahan kepada pengguna
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Menavigasi ke aktivitas pendaftaran (RegisterActivity)
    private fun openRegisterActivity() {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }

    // Menampilkan ProgressBar
    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    // Sembunyikan ProgressBar
    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}
