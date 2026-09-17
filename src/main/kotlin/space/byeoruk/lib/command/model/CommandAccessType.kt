package space.byeoruk.lib.command.model

import org.bukkit.command.BlockCommandSender
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.RemoteConsoleCommandSender
import org.bukkit.entity.Player

enum class CommandAccessType {
    PLAYER, CONSOLE, REMOTE_CONSOLE, COMMAND_BLOCK;

    companion object {
        fun of(sender: CommandSender): CommandAccessType? = when (sender) {
            is Player -> PLAYER
            is BlockCommandSender -> COMMAND_BLOCK
            is RemoteConsoleCommandSender -> REMOTE_CONSOLE
            is ConsoleCommandSender -> CONSOLE
            else -> null
        }
    }
}