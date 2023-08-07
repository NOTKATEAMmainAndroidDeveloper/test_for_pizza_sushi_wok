package com.ntk.test_for_pizza_sushi_wok.views.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ntk.test_for_pizza_sushi_wok.receivers.NetworkRecevier
import com.ntk.test_for_pizza_sushi_wok.viewmodels.SplashViewModel
import com.ntk.test_for_pizza_sushi_wok.views.custom.MultiSplashIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(controller: NavHostController) {
    val viewModel by remember { mutableStateOf(SplashViewModel()) }

    if(NetworkRecevier.is_online.value && viewModel.connection.value == null){
        SideEffect{
            CoroutineScope(Dispatchers.Main).launch {
                if(!viewModel.isExistDb(controller).isActive){
                    viewModel.isExistDb(controller).start()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column {
            MultiSplashIndicator()

            AnimatedVisibility(visible = !NetworkRecevier.is_online.value) {
                Text("Отсутствует соединение с интернетом", color = Color.Red, fontSize = 16.sp)
            }
        }
    }
}