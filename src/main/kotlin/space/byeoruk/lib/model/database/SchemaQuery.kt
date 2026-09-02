package space.byeoruk.lib.model.database

import space.byeoruk.lib.utility.database.ColumnUtility
import java.sql.Connection

interface SchemaQuery<T> where T : SchemaColumn, T : Enum<T> {
    val entries: List<T>
    val table: String

    val createTableQuery: String get() = "CREATE TABLE IF NOT EXISTS $table"

    fun columnsOf(exclude: Collection<T> = emptyList()): String =
        entries.filterNot { it in exclude }.joinToString(", ") { it.column() }

    fun paramsOf(exclude: Collection<T> = emptyList()): String =
        entries.filterNot { it in exclude }.joinToString(", ") { "?" }

    fun columnsWithType(useSqlite: Boolean): String =
        entries.joinToString(", ") { "${it.column()} ${if (!useSqlite) it.type() else it.typeForSQLite()}" }

    fun createQuery(useSqlite: Boolean): String

    fun syncColumns(connection: Connection, useSqlite: Boolean): List<String> =
        ColumnUtility.syncColumns(connection, table, entries, useSqlite)
}