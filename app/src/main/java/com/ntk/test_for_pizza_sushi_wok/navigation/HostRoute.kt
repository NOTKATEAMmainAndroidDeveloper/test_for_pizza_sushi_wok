package com.ntk.test_for_pizza_sushi_wok.navigation

sealed class HostRoute(val route: String) {
    object Splash: HostRoute("splash_screen")
    object Main: HostRoute("main_screen")
}