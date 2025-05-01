package com.example.csce546_project.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.csce546_project.models.FillBlankQuestion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

const val BLANK = "▭▭▭▭"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FillBlankScreen(
    question: FillBlankQuestion,
    onSubmit: (Boolean) -> Unit
) {
    data class Choice(val text: String, val index: Int)

    val segments = remember(question.text) { question.text.split(BLANK) }
    val totalBlanks = segments.size - 1

    var selected by remember { mutableStateOf<List<Choice>>(emptyList()) }
    var submitted by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    val shuffledChoices = remember(question) {
        val incorrect = question.incorrectChoices.mapIndexed { i, t -> Choice(t, -i - 1) }
        val correct = question.choices.mapIndexed { i, t -> Choice(t.text, i) }
        (incorrect + correct).shuffled()
    }

    fun handleSelect(choice: Choice) {
        if (selected.size < totalBlanks && !submitted) {
            selected = selected + choice
        }
    }

    fun handleSubmit() {
        if (selected.size != totalBlanks || submitted) return
        val correctCount = selected.countIndexed { i, c -> c.index == i }
        isCorrect = correctCount == totalBlanks
        submitted = true
    }

    fun handleClear() {
        selected = emptyList()
        submitted = false
    }

    fun handleNext() {
        selected = emptyList()
        onSubmit(isCorrect)
        isCorrect = false
        submitted = false
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Title
        if (!question.title.isNullOrEmpty()) {
            Text(
                text = question.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Fill-in-the-blank prompt
        FlowRow(
            modifier = Modifier.padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            segments.forEachIndexed { i, segment ->
                Text(text = segment, style = MaterialTheme.typography.bodyLarge)

                if (i < totalBlanks) {
                    val choice = selected.getOrNull(i)
                    Box(
                        modifier = Modifier
                            .background(
                                when {
                                    submitted && choice?.index == i -> Color(0xFFC8E6C9)
                                    submitted && choice != null -> Color(0xFFFFCDD2)
                                    else -> Color.LightGray
                                },
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = choice?.text ?: "____")
                    }
                }
            }
        }

        // Word bank
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            shuffledChoices.forEach { choice ->
                val alreadySelected = selected.contains(choice)
                Button(
                    onClick = { handleSelect(choice) },
                    enabled = !alreadySelected && !submitted,
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (alreadySelected) Color.Gray else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(choice.text)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { handleClear() },
                enabled = selected.isNotEmpty() && !submitted
            ) {
                Text("Clear")
            }

            Button(
                onClick = { handleSubmit() },
                enabled = selected.size == totalBlanks && !submitted
            ) {
                Text("Submit")
            }

            Button(
                onClick = { handleNext() },
                enabled = submitted,
            ) {
                Text("Next")
            }
        }
    }
}


inline fun <T> List<T>.countIndexed(predicate: (index: Int, item: T) -> Boolean): Int {
    var count = 0
    forEachIndexed { index, item ->
        if (predicate(index, item)) count++
    }
    return count
}
