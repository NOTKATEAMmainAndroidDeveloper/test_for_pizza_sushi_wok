package com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree

import androidx.core.text.isDigitsOnly
import com.github.jasync.sql.db.Connection
import com.google.gson.Gson
import com.ntk.test_for_pizza_sushi_wok.interfaces.PostgreeModelInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

suspend fun PostgreeModelInterface.editModelFromBase(connection: Connection) = coroutineScope {
    val gson = Gson()

    if (!connection.isConnected())connection.connect()

    launch {
        val modelJson = gson.toJson(this@editModelFromBase)

        val jsonObjectModel = JSONObject(modelJson)

        var annotationString = ""
        var valueString = ""

        val id = jsonObjectModel["id"]

        for(annot in jsonObjectModel.keys()){
            if(annot == "id") continue

            annotationString += "$annot, "

            val value = jsonObjectModel[annot]
            valueString += if(value.toString().isDigitsOnly())
                ("${jsonObjectModel[annot]}") else
                ("'${jsonObjectModel[annot]}'")

            valueString += ", "
        }

        val annStringLength = annotationString.length
        annotationString = annotationString.removeRange(annStringLength - 2, annStringLength - 1)

        val valStringLength = valueString.length
        valueString = valueString.removeRange(valStringLength - 2, valStringLength - 1)

        println("annotationString: $annotationString")
        println("valueString: $valueString")


        withContext(Dispatchers.IO) {
            connection.sendPreparedStatement(
                "UPDATE ${this@editModelFromBase.tableName} SET ($annotationString) " +
                        "= ($valueString) " +
                        "WHERE id = $id"
            ).get()
        }
    }
}