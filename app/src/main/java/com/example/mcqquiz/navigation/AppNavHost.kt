package com.example.mcqquiz.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mcqquiz.screens.McqScreen
import com.example.mcqquiz.screens.ResultScreenRoute
import com.example.mcqquiz.viewmodel.QuizViewModel
import kotlinx.serialization.Serializable

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: QuizViewModel
) {
    NavHost(navController = navController, startDestination = McqScreenRoute) {
        composable<McqScreenRoute> {
            McqScreen(viewModel = viewModel, modifier = modifier, onNavigateToResultsScreen = { navController.navigate(ResultScreenRoute) })
        }
        composable<ResultScreenRoute> {
            ResultScreenRoute(viewModel = viewModel, modifier = modifier, onRestartClick = { navController.popBackStack() })
        }
    }
}

@Serializable
object McqScreenRoute
@Serializable
object ResultScreenRoute