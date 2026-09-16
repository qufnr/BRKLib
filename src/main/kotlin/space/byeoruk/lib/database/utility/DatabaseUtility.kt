package space.byeoruk.lib.database.utility

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object DatabaseUtility {
    /**
     * DB 전용 단일 스레드 실행기 생성
     *
     * @param name 스레드 이름
     */
    @JvmStatic
    fun singleThread(name: String): ExecutorService =
        Executors.newSingleThreadExecutor { runnable ->
            Thread(runnable, name)
        }
}