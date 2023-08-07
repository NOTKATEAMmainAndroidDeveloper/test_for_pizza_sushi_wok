package com.ntk.test_for_pizza_sushi_wok.domain.usecase.postgree

import android.util.Log
import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.ConnectionPoolConfiguration
import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.general.ArrayRowData
import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.pool.PoolConfiguration
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory
import java.util.Properties
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

suspend fun getPostgreeConection(configuration: Configuration): Connection? {
    val poolConfiguration = ConnectionPoolConfiguration(
        maxActiveConnections = 10000,
        queryTimeout = 30,
        password = configuration.password,
        username = configuration.username,
        host = configuration.host,
        ssl = configuration.ssl,
        port = configuration.port,
    )


    return try {
        val connection: Connection = ConnectionPool(
            PostgreSQLConnectionFactory(configuration), poolConfiguration
        )

        val con = connection.asSuspending.connect().connect().join()

        con
    }catch (e: Exception){
        Log.e("SPLASH initializeDb", "Проблема при получении соединения: ${e.toString()}")

        null
    }
}