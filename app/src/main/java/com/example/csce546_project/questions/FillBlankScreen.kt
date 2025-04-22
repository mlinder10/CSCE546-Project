package com.example.csce546_project.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.csce546_project.models.FillBlankQuestion
import androidx.compose.ui.Modifier

@Composable
fun FillBlankScreen(question : FillBlankQuestion, onSubmit: () -> Unit) {
    Column {
        Text("Fill Blank Screen")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSubmit) {
            Text("Submit")
        }
    }

}