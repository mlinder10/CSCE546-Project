package com.example.csce546_project

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            // Fetch course and topics
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
                title = { Text(course.value?.name ?: "Topics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
fun TopicCard(topic: Topic) {
    Card(Modifier.padding(8.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(topic.name, fontWeight = FontWeight.Bold)
        }
    }
}