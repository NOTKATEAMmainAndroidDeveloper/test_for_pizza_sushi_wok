package com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree

import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.general.ArrayRowData
import com.google.gson.Gson

inline fun <reified T>QueryResult.proceedListToModel(onEmpty: () -> Unit = {}, onProcced: (T) -> Unit = {}){
    if(this.rows.size == 0){
        onEmpty()
        return
    }

    val gson = Gson()

    val keys = (this.rows[0] as ArrayRowData).mapping.keys.toList()
    for (i in 0 until this.rows.size ) {
        var local = -1
        val toJson = gson.toJson(
            (this.rows[i] as ArrayRowData).columns
                .toList().associateBy {
                    local ++
                    keys[local]
                }
        )

        onProcced(gson.fromJson(toJson, T::class.java))
    }
}