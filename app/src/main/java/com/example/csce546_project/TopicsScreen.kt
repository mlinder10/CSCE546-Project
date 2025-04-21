package com.example.csce546_project

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csce546_project.models.Course
import com.example.csce546_project.models.Topic
import com.example.csce546_project.network.RetrofitInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(sectionId: String, onBack: () -> Unit) {
    val topics = remember { mutableStateOf<List<Topic>>(emptyList()) }
    val course = remember { mutableStateOf<Course?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(sectionId) {
        try {
            course.value = RetrofitInstance.api.fetchCourseBySection(sectionId)
            topics.value = course.value?.topics ?: emptyList()
        } catch (e: Exception) {
            Log.e("TopicsScreen", "Error loading topics", e)
        } finally {
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(course.value?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(Modifier.padding(padding)) {
                items(topics.value) { topic ->
                    TopicCard(topic)
                }
            }
        }
    }
}

@Composable
fun TopicCard(
    topic: Topic,
    onQuestionTypeSelected: (topic: Topic, type: String) -> Unit = { _, _ -> }
) {
    val questionTypes = listOf("Multiple Choice", "Matching", "Word", "Fill in Blank")
    val expanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = { expanded.value = !expanded.value }
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = topic.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ExpandMore, modifier = Modifier.rotate(if (expanded.value) 0f else -90f), contentDescription = "Expand")
            }

            AnimatedVisibility(visible = expanded.value) {
                Column(Modifier.padding(top = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        questionTypes.forEach { type ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                            ) {
                                Button(
                                    onClick = { onQuestionTypeSelected(topic, type) },
                                    modifier = Modifier.fillMaxSize(),
                                    shape = MaterialTheme.shapes.medium,
                                    contentPadding = PaddingValues(4.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Icon(
                                            imageVector = getIcon(type),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = type,
                                            maxLines = 2,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getIcon(type: String) = when (type) {
    "Multiple Choice" -> Icons.Default.Help
    "Matching" -> Icons.Default.Shuffle
    "Word" -> Icons.Default.Edit
    "Fill in Blank" -> Icons.Default.FormatColorText
    else -> Icons.Default.Help
}
