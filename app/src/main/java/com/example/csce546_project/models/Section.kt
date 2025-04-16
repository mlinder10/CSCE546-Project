package com.example.csce546_project.models

import com.google.gson.annotations.SerializedName

data class Section(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("instructorId") val instructorId: String,
        @SerializedName("code") val code: String,
        @SerializedName("courseId") val courseId: String?
)
