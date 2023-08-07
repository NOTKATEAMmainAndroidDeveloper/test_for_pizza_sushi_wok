package com.ntk.test_for_pizza_sushi_wok.domain.usecase.system

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Composable
fun SetBarColor(window: Window, color: Color){
    SideEffect {
        window.statusBarColor = color.toArgb()
        window.navigationBarColor = color.toArgb()
    }
}