package com.example.csce546_project

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.csce546_project.models.Section
import com.example.csce546_project.network.RetrofitInstance
import com.example.csce546_project.network.TokenManager
import com.example.csce546_project.ui.theme.CSCE546ProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Hardcoded credentials
// Change these to get sections from a different account
private const val EMAIL = "jake@email.com"
private const val PASSWORD = "pass"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionsScreen() {
    val sections = remember { mutableStateOf<List<Section>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            if (TokenManager.token == null) {
                val credentials = mapOf(
                    "email" to EMAIL,
                    "password" to PASSWORD
                )

                val authResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.login(credentials)
                }

                TokenManager.token = authResponse.token
                Log.d("SectionsScreen", "Authentication successful")
            }

            val fetchedSections = withContext(Dispatchers.IO) {
                RetrofitInstance.api.fetchSections()
            }
            sections.value = fetchedSections
            Log.d("SectionsScreen", "Fetched ${fetchedSections.size} sections")
        } catch (e: Exception) {
            Log.e("SectionsScreen", "Error: ${e.message}", e)
            error.value = "Failed to load sections: ${e.message}"
        } finally {
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sections") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading.value -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                error.value != null -> {
                    Text(
                        text = error.value!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                sections.value.isEmpty() -> {
                    Text(
                        "No sections found",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    SectionList(sections = sections.value)
                }
            }
        }
    }
}

@Composable
fun SectionList(sections: List<Section>) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(sections) { section ->
            SectionCard(section = section)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionCard(section: Section) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = {
            Log.d("SectionCard", "Clicked on section: ${section.name}")
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = section.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionsScreenPreview() {
    CSCE546ProjectTheme {
        SectionsScreen()
    }
}