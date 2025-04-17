package com.example.csce546_project

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Sections.route
    ) {
        composable(Screen.Sections.route) {
            SectionsScreen { sectionId ->
                navController.navigate(Screen.Topics.createRoute(sectionId))
            }
        }
        composable(
            Screen.Topics.route,
            arguments = listOf(navArgument("sectionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getString("sectionId") ?: ""
            TopicsScreen(
                sectionId = sectionId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Sections : Screen("sections")
    object Topics : Screen("topics/{sectionId}") {
        fun createRoute(sectionId: String) = "topics/$sectionId"
    }
}