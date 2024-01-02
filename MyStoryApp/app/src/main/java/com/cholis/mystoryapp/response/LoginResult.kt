package com.cholis.mystoryapp.response

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("token")
    val token: String,
    @field:SerializedName("userId")
    val userId: String
)