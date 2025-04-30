package com.example.csce546_project.questions

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.csce546_project.models.WordQuestion

@Composable
fun WordScreen(
    question: WordQuestion,
    onSubmit: (Boolean) -> Unit
) {
    var userAnswer by remember { mutableStateOf("") }
    var isAnswered by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    val correctAnswers = question.answers.map { it.trim().lowercase() }
    val submitColor = when {
        !isAnswered -> Color(0xFF007AFF)
        isCorrect -> Color(0xFF4CAF50)
        else -> Color(0xFFF44336)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        if (!question.title.isNullOrEmpty()) {
            Text(
                text = question.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Optional Image
        if (!question.image.isNullOrEmpty()) {
            val imageBytes = Base64.decode(question.image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Image(
                bitmap = decodedImage.asImageBitmap(),
                contentDescription = "Question Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
            )
        }

        // Question Text
        Text(
            text = question.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Text Field
        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            enabled = !isAnswered,
            label = { Text("Type your answer here") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Correct Answer feedback
        if (isAnswered && !isCorrect) {
            Text(
                text = "Correct answer: ${question.answers.firstOrNull() ?: ""}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                if (isAnswered) return@Button

                isCorrect = correctAnswers.contains(userAnswer.trim().lowercase())
                isAnswered = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = submitColor
            ),
            enabled = !isAnswered,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when {
                    !isAnswered -> "Submit"
                    isCorrect -> "Correct!"
                    else -> "Incorrect"
                },
                color = Color.White
            )
        }

        // Next Button
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                userAnswer = ""
                isAnswered = false
                onSubmit(isCorrect)
                isCorrect = false
            },
            enabled = isAnswered,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}
