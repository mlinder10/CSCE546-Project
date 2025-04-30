package com.example.csce546_project.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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

    val textSplit = remember(question.text) { question.text.split(BLANK) }

    var selected by remember { mutableStateOf(listOf<Choice>()) }
    var submitted by remember { mutableStateOf(false) }

    val shuffledChoices = remember(question) {
        val incorrectChoices = question.incorrectChoices.mapIndexed { i, text -> Choice(text, -(i + 1)) }
        val correctChoices = question.choices.mapIndexed { i, text -> Choice(text.text, i) }
        (incorrectChoices + correctChoices).shuffled()
    }

    val maxSelections = question.choices.size

    fun handleSelect(choice: Choice) {
        if (selected.size >= maxSelections || submitted) return
        selected = selected + choice
    }

    fun handleSubmit() {
        if (selected.size != maxSelections || submitted) return
        val correctCount = selected.countIndexed { i, c -> c.index == i }
        submitted = true
        onSubmit(correctCount == maxSelections)
    }

    fun handleClear() {
        selected = emptyList()
        submitted = false
    }

    fun handleNext() {
        selected = emptyList()
        submitted = false
        onSubmit(false) // or move externally
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
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Prompt
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start
        ) {
            textSplit.forEachIndexed { i, segment ->
                Text(
                    text = segment,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (i < maxSelections) {
                    val choice = selected.getOrNull(i)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .background(
                                color = if (submitted) {
                                    when {
                                        choice == null -> Color.LightGray
                                        choice.index == i -> Color(0xFFC8E6C9) // green
                                        else -> Color(0xFFFFCDD2) // red
                                    }
                                } else {
                                    Color.LightGray
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    Text(text = choice.text)
                }
            }
        }

        // Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { handleClear() },
                enabled = !submitted && selected.isNotEmpty()
            ) {
                Text("Clear")
            }

            Button(
                onClick = { handleSubmit() },
                enabled = !submitted && selected.size == maxSelections
            ) {
                Text("Submit")
            }

            Button(
                onClick = { handleNext() },
                enabled = submitted,
                shape = CircleShape,
                modifier = Modifier.size(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (submitted) Color(0xFF007AFF) else Color.Gray
                )
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next", tint = Color.White)
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
