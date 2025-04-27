package com.example.csce546_project.questions

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.csce546_project.models.MultipleChoiceQuestion
import androidx.compose.ui.Modifier
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
fun MultipleChoiceScreen(question: MultipleChoiceQuestion, onSubmit: (Boolean) -> Unit) {
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isSelected by remember { mutableStateOf(false) }

    val onSelect = {choice: String ->
        selectedAnswer = choice
        isSelected = true
    }

    val shuffledChoices = remember(question) {
        (question.choices + question.answer).shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Question Title
        if (!question.title.isNullOrEmpty()) {
            Text(
                text = question.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Question Image
        if (!question.image.isNullOrEmpty()) {
            val imageBytes = Base64.decode(question.image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Image(
                bitmap = decodedImage.asImageBitmap(),
                contentDescription = "Question Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Question Text
        if (!question.text.isNullOrEmpty()) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Answer choices
        shuffledChoices.forEach { choice ->
            OutlinedButton(
                onClick = { if (!isSelected )onSelect(choice)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = !isSelected,
                colors = ButtonDefaults.outlinedButtonColors(
                    disabledContainerColor = when {
                        isSelected && choice == question.answer -> MaterialTheme.colorScheme.tertiaryContainer
                        choice == selectedAnswer && choice != question.answer -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Text(
                    text = choice,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        fun onNext() {
            onSubmit(selectedAnswer == question.answer)
            isSelected = false
            selectedAnswer = null
        }
        //Next button
        Button(onClick = {onNext()},
            enabled = isSelected
            )  {
            Text("Next")
        }
    }
}