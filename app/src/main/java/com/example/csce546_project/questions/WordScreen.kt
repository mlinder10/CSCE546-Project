package com.example.csce546_project.questions

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.csce546_project.models.WordQuestion

@Composable
fun WordScreen(question : WordQuestion, onSubmit: () -> Unit) {
    Text("Multiple Choice Screen")
    Button(onClick = onSubmit) {
        Text("Submit")
    }
}