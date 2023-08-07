package com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree

import com.github.jasync.sql.db.Connection
import com.google.gson.Gson
import com.ntk.test_for_pizza_sushi_wok.interfaces.PostgreeModelInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

suspend fun PostgreeModelInterface.deleteModelFromBase(connection: Connection) = coroutineScope {
    val gson = Gson()

    if (!connection.isConnected())connection.connect()

    launch {
        val modelJson = gson.toJson(this@deleteModelFromBase)

        val jsonObjectModel = JSONObject(modelJson)

        val id = jsonObjectModel["id"]

        withContext(Dispatchers.IO) {
            connection.sendPreparedStatement(
                "DELETE FROM ${this@deleteModelFromBase.tableName} WHERE id = $id"
            ).get()
        }
    }
}