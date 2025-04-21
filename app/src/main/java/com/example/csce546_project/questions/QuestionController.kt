package com.example.csce546_project.questions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.csce546_project.models.*

@Composable
fun QuestionScreen(
    questions: List<Question>,
    currentIndex: Int,
    onComplete: () -> Unit
) {
    var index by remember { mutableStateOf(currentIndex) }

    fun onSubmit() {
        if (index + 1 < questions.size) {
            index++
        } else {
            onComplete()
        }
    }

    when (val question = questions[index]) {
        is MultipleChoiceQuestion -> MultipleChoiceScreen(question, ::onSubmit)
        is FillBlankQuestion -> FillBlankScreen(question, ::onSubmit)
        is MatchingQuestion -> MatchingScreen(question, ::onSubmit)
        is WordQuestion -> WordScreen(question, ::onSubmit)
    }
}
