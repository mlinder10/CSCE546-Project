package com.example.csce546_project.questions

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.csce546_project.TopicCard
import com.example.csce546_project.models.*
import com.example.csce546_project.models.Question
import com.example.csce546_project.network.RetrofitInstance
import androidx.compose.material3.AlertDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    topicId: String,
    type: String,
    onBack: () -> Unit = {}
) {
    var index by remember { mutableStateOf(0) }
    val questions = remember { mutableStateListOf<Question>()}
    val isLoading = remember { mutableStateOf(true) }
    var numCorrect by remember { mutableStateOf(0) }
    var showResults by remember { mutableStateOf(false) }


    LaunchedEffect(topicId, type) {
        questions.clear()
        try {
            // Retrofit can't handle if type isn't explicitly stated so we need
            // separate api calls
            when (type) {
                "multiple-choice" -> {
                    questions.addAll(RetrofitInstance.api.fetchMCQuestions(topicId))
                }
                "matching" -> {
                    questions.addAll(RetrofitInstance.api.fetchMatchingQuestions(topicId))
                }
                "fill-blank" -> {
                    questions.addAll(RetrofitInstance.api.fetchFillBlankQuestions(topicId))
                }
                "word" -> {
                    questions.addAll(RetrofitInstance.api.fetchWordQuestions(topicId))
                }
                else -> {
                    Log.e("QuestionScreen", "Unknown question type: $type")
                }
            }
        } catch (e: Exception) {
            Log.e("QuestionsScreen", "Error loading Questions", e)
        } finally {
            isLoading.value = false
        }
    }

    fun onSubmit(isCorrect: Boolean) {
        if (isCorrect) {
            numCorrect++
        }
        if (index + 1 < questions.size) {
            index++
        } else {
           showResults = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (questions.isNotEmpty())
                         Text("Question " + (index + 1) + "/" + questions.size)
                },

                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                isLoading.value -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                questions.isEmpty() -> {
                    NotFoundScreen(onBack)
                }

                else -> {
                    when (val question = questions[index]) {
                        is MultipleChoiceQuestion -> MultipleChoiceScreen(question, onSubmit = { isCorrect -> onSubmit(isCorrect) })
                        is FillBlankQuestion -> FillBlankScreen(question, onSubmit = { isCorrect -> onSubmit(isCorrect) })
                        is MatchingQuestion -> MatchingScreen(question, onSubmit = { isCorrect -> onSubmit(isCorrect) })
                        is WordQuestion -> WordScreen(question, onSubmit = { isCorrect -> onSubmit(isCorrect) })
                    }
                }
            }
        }
        if (showResults) {
            NumCorrectAlert(
                numCorrect,
                questions.size,
                onDismiss = { showResults = false; onBack() }
            )
        }
    }
}

@Composable
fun NotFoundScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No Questions Found")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Go Back")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumCorrectAlert(
    numCorrect: Int,
    total: Int,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { Button(onClick = onDismiss) { Text("OK") } },
        title = { Text("Quiz Completed") },
        text = { Text("$numCorrect/$total correct") },
    )
}
