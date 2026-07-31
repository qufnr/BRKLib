package space.byeoruk.lib.utility.sound

import org.bukkit.Sound
import org.bukkit.entity.Player

object SoundUtility {
    fun Player.playFailSound() {
        this.playSound(this, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 2f)
    }

    fun Player.playOkSound() {
        this.playSound(this, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f)
    }

    fun Player.playClickSound() {
        this.playSound(this, Sound.UI_BUTTON_CLICK, 1f, 1.5f)
    }

    fun Player.playSystemSound() {
        this.playSound(this, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 2f)
    }
}