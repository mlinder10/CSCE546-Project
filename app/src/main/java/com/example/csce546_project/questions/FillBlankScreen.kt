package com.example.csce546_project.questions

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.csce546_project.models.FillBlankQuestion

@Composable
fun FillBlankScreen(question : FillBlankQuestion, onSubmit: () -> Unit) {
    Text("Fill Blank Screen")
    Button(onClick = onSubmit) {
        Text("Submit")
    }

}