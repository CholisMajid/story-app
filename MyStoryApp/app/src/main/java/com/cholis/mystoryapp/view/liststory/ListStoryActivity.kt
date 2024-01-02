package com.cholis.mystoryapp.view.liststory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cholis.mystoryapp.LoadingAdapter
import com.cholis.mystoryapp.R
import com.cholis.mystoryapp.databinding.ActivityListStoryBinding
import com.cholis.mystoryapp.preferences.Preferences
import com.cholis.mystoryapp.view.FactoryVM
import com.cholis.mystoryapp.view.addstory.AddStoryActivity
import com.cholis.mystoryapp.view.detail.DetailActivity
import com.cholis.mystoryapp.view.maps.MapsActivity
import com.cholis.mystoryapp.view.welcome.WelcomeActivity

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var adapter: AdapterStory

    private val listStoryViewModel: ListStoryVM by viewModels {
        FactoryVM(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvStory.layoutManager = layoutManager

        setupAdapter()

        listStoryViewModel.stories.observe(this) { data ->
            if (data != null) {
                showLoading(false)
                adapter.submitData(lifecycle, data)
            }
        }

        binding.addStoryButton.setOnClickListener {
            val addStoryIntent = Intent(this, AddStoryActivity::class.java)
            startActivity(addStoryIntent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                Preferences.logOut(this)
                val intent = Intent(
                    this,
                    WelcomeActivity::class.java
                ).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                )
                startActivity(intent)
                finish()
                return true
            }
            R.id.menu_maps -> {
                val intent = Intent(
                    this,
                    MapsActivity::class.java
                ).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                )
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupAdapter() {
        adapter = AdapterStory { story, imageView, nameView, dateView, descView ->
        }

        adapter.setOnItemClickListener { story ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("story", story)
            startActivity(intent)
        }

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter {
                adapter.retry()
            }
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
