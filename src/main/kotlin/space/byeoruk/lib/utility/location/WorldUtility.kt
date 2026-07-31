package space.byeoruk.lib.utility.location

import org.bukkit.World

object WorldUtility {
    fun World.formatName(): String = when (this.name) {
        "world_nether" -> "<color:#2a0506>네더</color>"
        "world_end" -> "<color:#1c1920>엔드</color>"
        else -> "<color:#82aeff>세계</color>"
    }
}