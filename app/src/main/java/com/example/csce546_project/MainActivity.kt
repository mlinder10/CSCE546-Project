package com.example.csce546_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.csce546_project.ui.theme.CSCE546ProjectTheme
import com.example.csce546_project.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSCE546ProjectTheme {
                    Navigation()
            }
        }
    }
}