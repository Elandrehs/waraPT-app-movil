package com.warapt.workers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.warapt.workers.data.local.SessionManager
import com.warapt.workers.ui.login.LoginScreen
import com.warapt.workers.ui.workers.AddWorkerScreen
import com.warapt.workers.ui.workers.WorkersScreen

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val startDestination = if (sessionManager.isLoggedIn()) Routes.WORKERS else Routes.LOGIN

    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

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
                onAddWorkerClick = { navController.navigate(Routes.ADD_WORKER) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }   // borra historial de nav
                    }
                }
            )
        }

        composable(Routes.ADD_WORKER) {
            AddWorkerScreen(
                onWorkerAdded = { navController.popBackStack() } // vuelve a Workers
            )
        }
    }
}