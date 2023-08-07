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


suspend fun PostgreeModelInterface.pushModelToBase(connection: Connection) = coroutineScope {
    val gson = Gson()

    if (!connection.isConnected())connection.connect()

    launch {
        val modelJson = gson.toJson(this@pushModelToBase)

        val jsonObjectModel = JSONObject(modelJson)

        var annotationString = ""
        var valueString = ""

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


        val future = withContext(Dispatchers.IO) {
            connection.sendPreparedStatement(
                "INSERT INTO ${this@pushModelToBase.tableName} " +
                        "($annotationString) " +
                        "VALUES ($valueString)"
            ).get()
        }
    }
}