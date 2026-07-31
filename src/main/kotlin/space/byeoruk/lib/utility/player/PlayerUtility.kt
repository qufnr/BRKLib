package space.byeoruk.lib.utility.player

import com.google.gson.Gson
import space.byeoruk.lib.dto.mojang.MojangPlayerSession
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.util.UUID

object PlayerUtility {
    private val gson = Gson()

    /**
     * Mojang 세션 서버에서 플레이어 정보 취득
     *
     * @param uuid 플레이어 UUID
     * @return 플레이어 세션 정보
     */
    fun fetchPlayerSession(uuid: UUID): MojangPlayerSession? {
        val trimmedUuid = uuid.toString().replace("-", "")
        val url = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/$trimmedUuid")
            .toURL()

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        return try {
            when (connection.responseCode) {
                200 -> connection.getInputStream().bufferedReader().use { reader ->
                    gson.fromJson(reader, MojangPlayerSession::class.java)
                }
                else -> null
            }
        }
        catch(e: IOException) {
            null
        }
        finally {
            connection.disconnect()
        }
    }

    /**
     * 플레이어 UUID 로 이름 조회
     *
     * @param uuid 플레이어 UUID
     * @return 플레이어 이름
     */
    fun fetchName(uuid: UUID): String? = fetchPlayerSession(uuid)?.name
}