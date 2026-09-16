package space.byeoruk.lib.database.manager

import org.bukkit.plugin.java.JavaPlugin
import java.lang.AutoCloseable
import java.sql.Connection
import java.sql.SQLException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Function
import java.util.function.Supplier
import java.util.logging.Logger
import javax.sql.DataSource

interface DatabaseManager {
    /**
     * 커넥션 풀
     */
    val dataSource: DataSource?

    /**
     * DB 전용 실행기. 단일 스레드여야 쓰기 순서가 보장되고, SQLite 잠금 경합이 없음.
     */
    val executor: ExecutorService

    /**
     * 플러그인 로거
     */
    val logger: Logger

    /**
     * 커넥션 풀에서 커넥션을 하나 꺼냄.
     *
     * 반환된 커넥션은 반드시 `use { }` 로 닫아야 풀로 돌아간다.
     *
     * @return DB 커넥션
     * @throws SQLException DataSource 가 초기화되지 않았을 때 던짐
     */
    fun newConnection(): Connection =
        dataSource?.connection ?: throw SQLException("DataSource is not initialized")

    /**
     * DB 작업을 전용 스레드에서 실행하고 결과를 받는다.
     *
     * @param block DB 작업
     * @return 작업 결과 Future
     */
    fun <T> submit(block: Supplier<T>): CompletableFuture<T> =
        CompletableFuture.supplyAsync(block, executor)
            .whenComplete { _, throwable ->
                throwable?.let {
                    logger.severe("DB 작업에 실패했습니다: ${it.message}")
                }
            }

    /**
     * 결과가 필요 없는 DB 작업을 전용 스레드에서 실행한다.
     *
     * @param block DB 작업
     * @return Void
     */
    fun execute(block: Runnable): CompletableFuture<Void> =
        CompletableFuture.runAsync(block, executor)
            .whenComplete { _, throwable ->
                throwable?.let {
                    logger.severe("DB 작업에 실패했습니다: ${it.message}")
                }
            }

    fun <T> transaction(block: Function<Connection, T>): T =
        newConnection().use { conn ->
            conn.autoCommit = false

            try {
                val result = block.apply(conn)
                conn.commit()
                result
            } catch (e: Exception) {
                runCatching {
                    conn.rollback()
                }
                throw e
            } finally {
                runCatching {
                    conn.autoCommit = true
                }
            }
        }

    fun closeDatabase(timeoutSeconds: Long) {
        executor.shutdown()

        if (!executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
            logger.warning("DB 작업이 ${timeoutSeconds}초 안에 끝나지 않아 강제 종료 처리됩니다")
            executor.shutdownNow()
        }

        (dataSource as? AutoCloseable)?.close()
    }
}