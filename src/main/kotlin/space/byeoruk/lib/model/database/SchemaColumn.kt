package space.byeoruk.lib.model.database

interface SchemaColumn {
    fun column(): String
    fun type(): String
    fun typeForSQLite(): String
    fun columnName(): String
}