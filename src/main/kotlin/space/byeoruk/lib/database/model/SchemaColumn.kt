package space.byeoruk.lib.database.model

interface SchemaColumn {
    fun column(): String
    fun type(): String
    fun typeForSQLite(): String? = null
    fun columnName(): String
}