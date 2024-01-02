package com.cholis.mystoryapp.view.register

import androidx.lifecycle.ViewModel
import com.cholis.mystoryapp.repository.RepoStory

class RegisterVM(private val repoAuth: RepoStory) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repoAuth.register(name, email, password)
}