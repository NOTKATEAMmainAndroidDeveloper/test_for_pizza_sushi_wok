package com.ntk.test_for_pizza_sushi_wok.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ntk.test_for_pizza_sushi_wok.views.screens.MainScreen
import com.ntk.test_for_pizza_sushi_wok.views.screens.SplashScreen

@Composable
fun AppNavigation() {
    val controller = rememberNavController()

    NavHost(
        navController = controller,
        startDestination = HostRoute.Splash.route
    ){
        composable(HostRoute.Splash.route){ SplashScreen(controller) }
        composable(HostRoute.Main.route){ MainScreen(controller) }
    }
}