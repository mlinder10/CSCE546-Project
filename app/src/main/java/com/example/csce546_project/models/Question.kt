package com.example.csce546_project.models

import com.google.gson.annotations.SerializedName

sealed interface Question {
  val id: String
  val title: String
  val hint: String
  val topicId: String
}

data class MultipleChoiceQuestion(
        @SerializedName("id") override val id: String,
        @SerializedName("title") override val title: String,
        @SerializedName("hint") override val hint: String,
        @SerializedName("topicId") override val topicId: String,
        @SerializedName("image") val image: String?,
        @SerializedName("text") val text: String?,
        @SerializedName("answer") val answer: String,
        @SerializedName("choices") val choices: List<String>,
        @SerializedName("type") val type: String = "multiple-choice"
) : Question

data class MatchingQuestion(
        @SerializedName("id") override val id: String,
        @SerializedName("title") override val title: String,
        @SerializedName("hint") override val hint: String,
        @SerializedName("topicId") override val topicId: String,
        @SerializedName("relations") val relations: List<List<String>>, // Pair was throwing an error, switched to a List instead
        @SerializedName("type") val type: String = "matching"
) : Question

data class WordQuestion(
        @SerializedName("id") override val id: String,
        @SerializedName("title") override val title: String,
        @SerializedName("hint") override val hint: String,
        @SerializedName("topicId") override val topicId: String,
        @SerializedName("image") val image: String?,
        @SerializedName("text") val text: String,
        @SerializedName("answers") val answers: List<String>,
        @SerializedName("type") val type: String = "word"
) : Question

data class FillBlankQuestion(
        @SerializedName("id") override val id: String,
        @SerializedName("title") override val title: String,
        @SerializedName("hint") override val hint: String,
        @SerializedName("topicId") override val topicId: String,
        @SerializedName("text") val text: String,
        @SerializedName("choices") val choices: List<Choice>,
        @SerializedName("incorrectChoices") val incorrectChoices: List<String>,
        @SerializedName("type") val type: String = "fill-blank"
) : Question

data class Choice(
        @SerializedName("text") val text: String,
        @SerializedName("index") val index: Int
)
