package com.example.csce546_project.questions

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.csce546_project.models.MatchingQuestion

@Composable
fun MatchingScreen(question : MatchingQuestion, onSubmit: () -> Unit) {
    Text("Multiple Choice Screen")
    Button(onClick = onSubmit) {
        Text("Submit")
    }
}