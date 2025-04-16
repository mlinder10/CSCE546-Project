package com.example.csce546_project.models

import com.google.gson.annotations.SerializedName

data class Topic(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("position") val position: Int,
        @SerializedName("mcq") val mcq: Int,
        @SerializedName("mcq") val mq: Int,
        @SerializedName("mcq") val wq: Int,
        @SerializedName("mcq") val fbq: Int
)
