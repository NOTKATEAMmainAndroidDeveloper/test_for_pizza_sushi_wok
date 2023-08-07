package com.ntk.test_for_pizza_sushi_wok

import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.SSLConfiguration
import com.ntk.test_for_pizza_sushi_wok.domain.usecase.system.SetBarColor
import com.ntk.test_for_pizza_sushi_wok.navigation.AppNavigation
import com.ntk.test_for_pizza_sushi_wok.receivers.NetworkRecevier
import com.ntk.test_for_pizza_sushi_wok.ui.theme.Test_for_pizza_sushi_wokTheme

class MainActivity : ComponentActivity(), NetworkRecevier.ConnectivityReceiverListener  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try{
            registerReceiver(NetworkRecevier(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            NetworkRecevier.connectivityReceiverListener = this
        }catch (_:java.lang.Exception){

        }

        setContent {
            SetBarColor(this.window, Color.Black)
            Test_for_pizza_sushi_wokTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(25,25,25)
                ) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        NetworkRecevier.is_online.value = isConnected
    }

    companion object{
        val conf = Configuration(
            username = "ntk.team.developer",
            password = "BTMC5Ufousz9",
            host = "ep-patient-lab-56100040.ap-southeast-1.aws.neon.tech",
            database = "testforpizzasushiwok",
            ssl = SSLConfiguration(
                mode = SSLConfiguration.Mode.Require
            ),

        )
    }
}