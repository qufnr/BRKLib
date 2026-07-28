package space.byeoruk.lib.command

import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.RemoteConsoleCommandSender
import org.bukkit.entity.Player

interface ExecuteCommand {
    val names: List<String>
    val description: String
        get() = "No description."
    val permission: String
        get() = "*"
    val accessSenderType: CommandAccessSenderType
        get() = CommandAccessSenderType.ALL

    fun execute(sender: CommandSender, args: Array<out String>)

    /**
     * 이 명령어에 접근할 수 있는지 여부 반환
     *
     * @param sender 명령어 전송한 개체
     * @param checkPermission 권한 확인 (PLAYER의 경우에만 확인)
     * @return 명령어에 접근할 수 있으면 true 아니면 false 반환
     */
    fun canAccess(sender: CommandSender, checkPermission: Boolean = false): Boolean =
        when(accessSenderType) {
            CommandAccessSenderType.PLAYER -> {
                val player = sender as? Player ?: return false
                !checkPermission || player.hasPermission(permission)
            }
            CommandAccessSenderType.OP -> {
                val player = sender as? Player ?: return false
                player.isOnline && player.isOp
            }
            CommandAccessSenderType.COMMAND_BLOCK -> sender is BlockCommandSender
            CommandAccessSenderType.CONSOLE -> sender is ConsoleCommandSender
            CommandAccessSenderType.REMOTE_CONSOLE -> sender is RemoteConsoleCommandSender
            else -> true
        }
}