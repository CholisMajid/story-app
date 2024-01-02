package com.cholis.mystoryapp.view.maps

import androidx.lifecycle.ViewModel
import com.cholis.mystoryapp.repository.RepoStory

class MapsVM(private val repoStory: RepoStory) : ViewModel() {
    fun getStoryWithLocation() = repoStory.getStoryWithLocation()
}