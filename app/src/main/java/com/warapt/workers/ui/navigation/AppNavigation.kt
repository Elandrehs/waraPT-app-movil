package com.warapt.workers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.warapt.workers.ui.login.LoginScreen
import com.warapt.workers.ui.workers.AddWorkerScreen
import com.warapt.workers.ui.workers.WorkersScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // navega a Workers y BORRA el login del historial,
                    // así el botón "atrás" no regresa al login
                    navController.navigate(Routes.WORKERS) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.WORKERS) {
            WorkersScreen(
                onAddWorkerClick = { navController.navigate(Routes.ADD_WORKER) }
            )
        }

        composable(Routes.ADD_WORKER) {
            AddWorkerScreen(
                onWorkerAdded = { navController.popBackStack() } // vuelve a Workers
            )
        }
    }
}