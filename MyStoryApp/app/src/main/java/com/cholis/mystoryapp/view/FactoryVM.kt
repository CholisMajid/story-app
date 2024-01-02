package com.cholis.mystoryapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cholis.mystoryapp.injection.Injection
import com.cholis.mystoryapp.view.addstory.AddStoryVM
import com.cholis.mystoryapp.view.liststory.ListStoryVM
import com.cholis.mystoryapp.view.login.LoginVM
import com.cholis.mystoryapp.view.maps.MapsVM
import com.cholis.mystoryapp.view.register.RegisterVM

class FactoryVM(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterVM::class.java) -> {
                RegisterVM(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginVM::class.java) -> {
                LoginVM(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(ListStoryVM::class.java) -> {
                ListStoryVM(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(AddStoryVM::class.java) -> {
                AddStoryVM(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(MapsVM::class.java) -> {
                MapsVM(Injection.provideRepository(context)) as T
            }else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}