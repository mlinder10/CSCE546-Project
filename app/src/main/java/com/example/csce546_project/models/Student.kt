package com.example.csce546_project.models

import com.google.gson.annotations.SerializedName

data class Student(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("displayName") val displayName: String,
        @SerializedName("email") val email: String,
        @SerializedName("token") val token: String?,
        @SerializedName("profilePic") val profilePic: String
)
