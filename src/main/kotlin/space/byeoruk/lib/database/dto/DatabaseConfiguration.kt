package space.byeoruk.lib.database.dto

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

data class DatabaseConfiguration(
    val enabled: Boolean = false,
    val url: String = "localhost:3306",
    val name: String,
    val driverClassname: String = "com.mysql.cj.jdbc.Driver",
    val username: String,
    val password: String,
    val maximumPoolSize: Int = 10,
    val minimumIdle: Int = 5,
    val connectionTimeout: Long = 10000,
    val idleTimeout: Long = 600000
) {
    companion object {
        fun build(plugin: JavaPlugin, path: String): DatabaseConfiguration {
            val config = plugin.config

            val enabled = config.getBoolean("$path.enabled", false)
            val name = config.getString("$path.name")
            val username = config.getString("$path.username")
            val password = config.getString("$path.password")

            if (enabled) {
                if (name.isNullOrEmpty()) {
                    plugin.logger.warning("데이터베이스 이름이 지정되지 않았습니다")
                }
                if (username.isNullOrEmpty()) {
                    plugin.logger.warning("데이터베이스 사용자 명이 지정되지 않았습니다")
                }
                if (password.isNullOrEmpty()) {
                    plugin.logger.warning("데이터베이스 비밀번호가 지정되지 않았습니다")
                }
            }

            return DatabaseConfiguration(
                enabled = config.getBoolean("$path.enabled", false),
                url = config.getString("$path.url", "localhost:3306") ?: "localhost:3306",
                name = name ?: "",
                driverClassname = config.getString("$path.driver-class-name", "com.mysql.cj.jdbc.Driver") ?: "com.mysql.cj.jdbc.Driver",
                username = username ?: "",
                password = password ?: "",
                maximumPoolSize = config.getInt("$path.maximum-pool-size", 10),
                minimumIdle = config.getInt("$path.minimum-idle", 5),
                connectionTimeout = config.getLong("$path.connection-timeout", 10000),
                idleTimeout = config.getLong("$path.idle-timeout", 600000)
            )
        }
    }
}