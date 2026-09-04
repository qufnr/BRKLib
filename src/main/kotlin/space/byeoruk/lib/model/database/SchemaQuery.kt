package space.byeoruk.lib.model.database

import java.sql.Connection
import java.sql.SQLException

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

    /**
     * 해당 스키마의 컬럼을 동기화 한다.
     *
     * @param connection DB 커넥션
     * @param useSqlite SQLite 사용 여부
     * @return 동기화 후 추가된 컬럼이 있으면 추가 컬럼 이름을 리스트 형태로 반환
     * @throws SQLException 테이블을 찾지 못한 경우. `CREATE TABLE` 직후에 호출하는 것을 전제로 하므로, 이 경우는 설정이 잘못된 상태로 보고 조용히 넘어가지 않음.
     */
    fun syncColumns(connection: Connection, useSqlite: Boolean): List<String> {
        //  실제 DB의 컬럼 이름 모으기
        val existing = mutableSetOf<String>()
        connection.metaData.getColumns(connection.catalog, null, table, null).use { resultSet ->
            while (resultSet.next()) {
                //  getColumns의 테이블 인자는 이름이 아니라 패턴이고, '_'는 "아무 글자 하나" 와일드카드다.
                //  season_shop 이 seasonXshop 같은 다른 테이블에도 걸리므로 이름을 다시 확인한다
                if (!resultSet.getString("TABLE_NAME").equals(table, ignoreCase = true)) {
                    continue
                }

                existing += resultSet.getString("COLUMN_NAME").lowercase()
            }
        }

        //  테이블 자체를 찾지 못한 경우. CREATE TABLE이 실행되지 않았거나, 카탈로그 · 대소문자가 어긋난 상태다
        if (existing.isEmpty()) {
            throw SQLException("테이블 '$table' 을(를) 찾을 수 없어 컬럼을 동기화할 수 없습니다.")
        }

        //  Enum에는 있고 DB에 없는 컬럼만 남김
        val addedColumnNames = mutableListOf<String>()
        val missing = entries.filterNot { it.column().lowercase() in existing }
        if (missing.isNotEmpty()) {
            //  컬럼 추가
            connection.createStatement().use { statement ->
                missing.forEach { c ->
                    val column = c.column()
                    val type = if (!useSqlite) c.type() else c.typeForSQLite()
                    statement.executeUpdate("ALTER TABLE $table ADD COLUMN $column $type")
                    addedColumnNames.add(column)
                }
            }
        }

        return addedColumnNames
    }
}