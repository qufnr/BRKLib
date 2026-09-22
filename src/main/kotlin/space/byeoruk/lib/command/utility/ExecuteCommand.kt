package space.byeoruk.lib.command.utility

import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.command.ProxiedCommandSender
import org.bukkit.entity.Player
import space.byeoruk.lib.MainPlugin
import space.byeoruk.lib.command.model.CommandAccessType
import java.util.EnumSet

interface ExecuteCommand {
    val names: List<String>

    val description: String get() = "No description"

    val permission: String? get() = null

    val accessTypes: Set<CommandAccessType> get() = EnumSet.allOf(CommandAccessType::class.java)

    fun suggest(sender: CommandSender, args: Array<String>): List<String> = emptyList()

    fun execute(sender: CommandSender, args: Array<String>)

    fun canAccess(sender: CommandSender): Boolean {
        val target =
            //  /execute as, 커맨드 마인카트 등으로 감싸져 들어온 경우 실제 대상을 꺼낸다
            (sender as? ProxiedCommandSender)?.callee
                ?: sender

        val type = CommandAccessType.of(target) ?: return false

        if (type !in accessTypes) {
            return false
        }

        val node = permission ?: return true
        return sender.hasPermission(node)
    }

    /**
     * 인자에서 접속 중인 플레이어를 찾는다
     *
     * @param arg 인자
     * @param ignoreCase 대소문자 구분 여부
     * @return 플레이어 객체, 찾을 수 없다면 null 반환
     */
    fun getPlayerFromArgument(arg: String, ignoreCase: Boolean = false): Player? {
        return MainPlugin.plugin.server.onlinePlayers
            .firstOrNull { it.name.equals(arg, ignoreCase) }
    }

    /**
     * 인자에서 플레이어를 찾는다
     *
     * 접속 중이 아니라면 서버에 접속한 적이 있는 오프라인 플레이어를 찾는다
     * 접속 중인지 확인하려면 [OfflinePlayer.isOnline] 또는 `as? Player`를 사용한다
     *
     * @param arg 인자
     * @param ignoreCase 대소문자 구분 여부
     * @return 플레이어 객체, 찾을 수 없다면 null 반환
     */
    fun getOfflinePlayerFromArgument(arg: String, ignoreCase: Boolean = false): OfflinePlayer? {
        getPlayerFromArgument(arg, ignoreCase)?.let { return it }

        //  프로필 캐시(usercache.json)에서 이름으로 조회한다. 대소문자를 구분하지 않는다
        MainPlugin.plugin.server.getOfflinePlayerIfCached(arg)
            ?.takeIf { ignoreCase || it.name == arg }
            ?.let { return it }

        //  캐시에 없다면 플레이어 데이터 전체를 훑는다
        return MainPlugin.plugin.server.offlinePlayers
            .firstOrNull { it.name.equals(arg, ignoreCase) }
    }
}