package com.ntk.test_for_pizza_sushi_wok.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.Connection
import com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree.getPostgreeConection
import com.ntk.test_for_pizza_sushi_wok.navigation.HostRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel: ViewModel() {
    var connection: MutableState<Connection?> = mutableStateOf(null)

    fun isExistDb(controller: NavHostController) = viewModelScope.launch{
        val conf = Configuration(
            username = "ntk.team.developer",
            password = "BTMC5Ufousz9",
            host = "ep-patient-lab-56100040.ap-southeast-1.aws.neon.tech",
            database = "testforpizzasushiwok",
        )

        val _connection: Connection? =
            withContext(Dispatchers.IO) {
                getPostgreeConection(conf)?.connect()!!.join()
            }

        if(_connection != null && _connection.isConnected()){

            connection.value = _connection

            withContext(Dispatchers.IO) {
                _connection.disconnect().get()
            }

            controller.navigate(HostRoute.Main.route)
        }
    }
}