package com.cholis.mystoryapp.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cholis.mystoryapp.databinding.ActivityWelcomeBinding
import com.cholis.mystoryapp.preferences.Preferences
import com.cholis.mystoryapp.view.liststory.ListStoryActivity
import com.cholis.mystoryapp.view.login.LoginActivity
import com.cholis.mystoryapp.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    // Mendeklarasikan variabel binding untuk mengikat tampilan layout ActivityAuthBinding.
    private lateinit var binding: ActivityWelcomeBinding

    // Mendefinisikan konstanta untuk kunci Shared Preferences.
    companion object {
        const val SHARED_PREF_KEY = "onSignIn"
        const val TOKEN_KEY = "token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi tampilan layout ActivityAuthBinding menggunakan inflasi layout.
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()

        // Menambahkan klik listener ke tombol login untuk menavigasi ke LoginActivity.
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Menambahkan klik listener ke tombol signup untuk menavigasi ke SignupActivity.
        binding.signupButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Menginisialisasi Shared Preferences dan mengambil token yang disimpan.
        val sharedPref = Preferences.initPref(this, SHARED_PREF_KEY)
        val token = sharedPref.getString(TOKEN_KEY, "")

        // Jika token tidak null atau tidak kosong, maka navigasi ke MainActivity.
        if (!token.isNullOrBlank()) {
            val intent = Intent(this, ListStoryActivity::class.java)
            startActivity(intent)

            // Opsional, untuk menutup AuthActivity jika token valid ditemukan.
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        // Menginisialisasi Shared Preferences dan mengambil token yang disimpan.
        val sharedPref = Preferences.initPref(this, SHARED_PREF_KEY)
        val token = sharedPref.getString(TOKEN_KEY, "")

        // Jika token tidak null atau tidak kosong, maka navigasi ke ListStoryActivity.
        if (!token.isNullOrBlank()) {
            val intent = Intent(this, ListStoryActivity::class.java)
            startActivity(intent)

            // Opsional, untuk menutup WelcomeActivity jika token valid ditemukan.
            finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(500)
        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }
        AnimatorSet().apply {
            playSequentially(title, desc, together)
            start()
        }
    }
}
