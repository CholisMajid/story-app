package com.cholis.mystoryapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.cholis.mystoryapp.PagingStory
import com.cholis.mystoryapp.api.ApiService
import com.cholis.mystoryapp.response.RegisterResponse
import com.cholis.mystoryapp.Result
import com.cholis.mystoryapp.response.AddStoryResponse
import com.cholis.mystoryapp.response.GetStoryResponse
import com.cholis.mystoryapp.response.LoginResponse
import com.cholis.mystoryapp.response.Story
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class RepoStory(private val apiService: ApiService) {

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }
    }

    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { PagingStory(apiService) }).liveData
    }

    fun addStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addStory(file, description)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("AddStoryViewModel", "addStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoryWithLocation(): LiveData<Result<GetStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation(1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("ListStoryViewModel", "getStoryWithLocation: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
}