package com.cholis.mystoryapp.view.addstory

import androidx.lifecycle.ViewModel
import com.cholis.mystoryapp.repository.RepoStory
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryVM(private val repoStory: RepoStory) : ViewModel() {
    fun addStory(file: MultipartBody.Part, description: RequestBody) =
        repoStory.addStory(file, description)
}