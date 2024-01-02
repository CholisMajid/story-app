package com.cholis.mystoryapp.view.detail

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cholis.mystoryapp.FormatDate
import com.cholis.mystoryapp.databinding.ActivityDetailBinding
import com.cholis.mystoryapp.response.Story

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        // Mengambil objek Story dari intent
        val story: Story? = intent.getParcelableExtra("story")

        if (story != null) {
            // Menampilkan foto menggunakan Glide
            Glide.with(this)
                .load(story.photoUrl)
                .into(binding.ivDetailStory)

            // Menampilkan nama, tanggal, dan deskripsi
            binding.tvDetailStoryName.text = story.name
            binding.tvDetailStoryDate.text = FormatDate.formatDate(story.createdAt, "UTC")
            binding.tvDetailStoryDesc.text = story.description
        }
    }
}
