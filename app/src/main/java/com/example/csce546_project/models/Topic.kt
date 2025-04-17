package com.example.csce546_project.models

import com.google.gson.annotations.SerializedName

data class Topic(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("position") val position: Int,
        @SerializedName("mcq") val mcq: List<MultipleChoiceQuestion>,
        @SerializedName("mq") val mq: List<MatchingQuestion>,
        @SerializedName("wq") val wq: List<WordQuestion>,
        @SerializedName("fbq") val fbq: List<FillBlankQuestion>
)
