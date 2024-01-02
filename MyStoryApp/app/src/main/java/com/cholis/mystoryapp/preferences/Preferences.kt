package com.cholis.mystoryapp.preferences

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    fun initPref(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
    private fun userPreference(context: Context, name: String): SharedPreferences.Editor {
        val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return pref.edit()
    }
    fun saveToken(token: String, context: Context) {
        val user = userPreference(context, "onSignIn")
        user.putString("token", token)
        user.apply()
    }
    fun logOut(context: Context) {
        val user = userPreference(context, "onSignIn")
        user.remove("token")
        user.remove("status")
        user.apply()
    }
}