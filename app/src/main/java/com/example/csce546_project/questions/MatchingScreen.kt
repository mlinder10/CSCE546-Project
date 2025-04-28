package com.example.csce546_project.questions

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.csce546_project.models.MatchingQuestion

@Composable
fun MatchingScreen(question: MatchingQuestion, onSubmit: (Boolean) -> Unit) {
    var selectedLeftIndex by remember { mutableStateOf<Int?>(null) }
    var selectedRightIndex by remember { mutableStateOf<Int?>(null) }
    var matches by remember { mutableStateOf<Map<Int, Pair<Int, Boolean>>>(emptyMap()) } // Key: right index, Value: (left index, isCorrect)
    var allMatchesMade by remember { mutableStateOf(false) }

    // Left side: image, Right side: text
    val leftItems = remember(question) { question.relations.map { it[0] } }
    val rightItems = remember(question) { question.relations.map { it[1] }.shuffled() }

    // Check if a match is correct
    fun isMatchCorrect(leftIndex: Int, rightIndex: Int): Boolean {
        return rightItems[rightIndex] == question.relations[leftIndex][1]
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = question.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Matching area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left column (images)
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                leftItems.forEachIndexed { index, image ->
                    MatchingImage(
                        imageData = image,
                        isSelected = selectedLeftIndex == index,
                        isCorrect = matches.values.any { it.first == index && it.second },
                        isIncorrect = matches.values.any { it.first == index && !it.second },
                        onClick = {
                            if (!matches.values.any { it.first == index }) {
                                selectedLeftIndex = if (selectedLeftIndex == index) null else index
                                if (selectedLeftIndex != null && selectedRightIndex != null) {
                                    val isCorrect = isMatchCorrect(selectedLeftIndex!!, selectedRightIndex!!)
                                    matches = matches + (selectedRightIndex!! to (selectedLeftIndex!! to isCorrect))
                                    selectedLeftIndex = null
                                    selectedRightIndex = null
                                    allMatchesMade = matches.size == leftItems.size
                                }
                            }
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right column (texts)
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                rightItems.forEachIndexed { index, text ->
                    MatchingText(
                        text = text,
                        isSelected = selectedRightIndex == index,
                        isCorrect = matches[index]?.second == true,
                        isIncorrect = matches[index]?.second == false,
                        onClick = {
                            if (!matches.containsKey(index)) {
                                selectedRightIndex = if (selectedRightIndex == index) null else index
                                if (selectedLeftIndex != null && selectedRightIndex != null) {
                                    val isCorrect = isMatchCorrect(selectedLeftIndex!!, selectedRightIndex!!)
                                    matches = matches + (selectedRightIndex!! to (selectedLeftIndex!! to isCorrect))
                                    selectedLeftIndex = null
                                    selectedRightIndex = null
                                    allMatchesMade = matches.size == leftItems.size
                                }
                            }
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next button
        Button(
            onClick = {
                val allCorrect = matches.values.all { it.second }
                onSubmit(allCorrect)
                selectedLeftIndex = null
                selectedRightIndex = null
                matches = emptyMap()
                allMatchesMade = false
            },
            enabled = allMatchesMade,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Next")
        }
    }
}

@Composable
private fun MatchingImage(
    imageData: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isIncorrect: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isCorrect -> MaterialTheme.colorScheme.tertiaryContainer
        isIncorrect -> MaterialTheme.colorScheme.errorContainer
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        modifier = modifier
            .background(backgroundColor, MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        val imageBytes = Base64.decode(imageData, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        Image(
            bitmap = decodedImage.asImageBitmap(),
            contentDescription = "Image to match",
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun MatchingText(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isIncorrect: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isCorrect -> MaterialTheme.colorScheme.tertiaryContainer
        isIncorrect -> MaterialTheme.colorScheme.errorContainer
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Box(
        modifier = modifier
            .size(120.dp)
            .fillMaxWidth()
            .background(backgroundColor, MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}