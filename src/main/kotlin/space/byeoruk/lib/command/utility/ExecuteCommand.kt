package space.byeoruk.lib.command.utility

import org.bukkit.command.CommandSender
import org.bukkit.command.ProxiedCommandSender
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
}