package space.byeoruk.lib.utility.command

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object CommandUtility {
    private val mm = MiniMessage.miniMessage()

    /**
     * 입력된 문자열에서 플레이어 반환
     *
     * @param sender 명령어 전송 플레이어
     * @param arg 명령어 인자
     * @param canSelf 전송 플레이어를 지목할 수 있는지 여부
     * @param messagePrefix 실패 메시지 접두사
     * @return 대상 플레이어 객체. 찾는 데 실패했을 경우 null 반환
     */
    fun findOpponent(sender: Player, arg: String, canSelf: Boolean, messagePrefix: String): Player? {
        val opponent = Bukkit.getPlayer(arg)
        if (opponent == null || !opponent.isOnline) {
            sender.sendMessage { mm.deserialize("${messagePrefix}대상을 찾을 수 없어요") }
            return null
        }

        if (!canSelf && sender == opponent) {
            sender.sendMessage { mm.deserialize("${messagePrefix}자신을 지정할 수 없어요") }
            return null
        }

        return opponent
    }

    /**
     * 입력된 문자열을 숫자로 반환
     *
     * @param sender 명령어 전송 플레이어
     * @param arg 명령어 인자
     * @param messagePrefix 실패 메시지 접두사
     * @return 입력 숫자. 숫자 변환 실패 시 null 반환
     */
    fun getNumber(sender: Player, arg: String, messagePrefix: String): Int? {
        val value = try {
            arg.toInt()
        }
        catch (e: NumberFormatException) {
            null
        }

        if (value == null) {
            sender.sendMessage { mm.deserialize("${messagePrefix}숫자를 입력해 주세요") }
            return null
        }

        return value
    }

    /**
     * 입력된 숫자를 양수로 가져옴
     *
     * @param sender 명령어 전송 플레이어
     * @param arg 명령어 인자
     * @param messagePrefix 실패 메시지 접두사
     * @return 입력 숫자. 숫자 변환 실패 및 숫자가 음수일 시 null 반환
     */
    fun getPositiveNumber(sender: Player, arg: String, messagePrefix: String): Int? {
        val value = getNumber(sender, arg, messagePrefix) ?: return null

        if (value < 0) {
            sender.sendMessage { mm.deserialize("${messagePrefix}음수로 입력할 수 없어요") }
            return null
        }

        return value
    }
}