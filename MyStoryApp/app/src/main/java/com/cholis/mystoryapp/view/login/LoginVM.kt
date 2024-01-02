package com.cholis.mystoryapp.view.login

import androidx.lifecycle.ViewModel
import com.cholis.mystoryapp.repository.RepoStory

class LoginVM(private val repoAuth: RepoStory) : ViewModel() {
    fun login(email: String, password: String) = repoAuth.login(email, password)
}