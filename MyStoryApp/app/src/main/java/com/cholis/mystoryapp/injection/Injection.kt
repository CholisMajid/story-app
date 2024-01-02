package com.cholis.mystoryapp.injection

import android.content.Context
import com.cholis.mystoryapp.api.ApiConfig
import com.cholis.mystoryapp.preferences.Preferences
import com.cholis.mystoryapp.repository.RepoStory
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): RepoStory {
        val pref = Preferences.initPref(context, "onSignIn")
        val user = runBlocking { pref.getString("token", null).toString() }
        val apiService = ApiConfig().getApiService(user)
        return RepoStory(apiService)
    }
}