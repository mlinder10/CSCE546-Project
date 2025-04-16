package com.example.csce546_project.models

import com.google.gson.annotations.SerializedName

data class Course(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("instructorId") val instructorId: String,
        @SerializedName("topics") val topics: List<Topic>
)
