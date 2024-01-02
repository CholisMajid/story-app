package com.cholis.mystoryapp.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cholis.mystoryapp.databinding.ActivityMainBinding
import com.cholis.mystoryapp.view.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.addStoryButton.setOnClickListener {
            val addStoryIntent = Intent(this, LoginActivity::class.java)
            startActivity(addStoryIntent)
            finish()
        }
    }
}
