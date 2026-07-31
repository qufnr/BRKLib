package space.byeoruk.lib.utility.exp

import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import kotlin.math.roundToInt

object ExpUtility {
    /**
     * 플레이어의 현재 까지 획득한 총 경험치 반환
     *
     * @return 총 경험치
     */
    fun Player.getTotalExp(): Int {
        val lvl = this.level
        val progress = (this.exp * this.expToLevel).roundToInt()

        val totalExpFromLevels = when {
            lvl <= 15 -> lvl * lvl + 6 * lvl    //  level^2 + 6 × level
            lvl <= 31 -> (2.5 * lvl * lvl - 40.5 * lvl + 360).toInt()   //  2.5 × level^2 – 40.5 × level + 360
            else -> (4.5 * lvl * lvl - 162.5 * lvl + 2220).toInt()  //  4.5 × level^2 – 162.5 × level + 2220
        }

        return totalExpFromLevels + progress
    }

    /**
     * 플레이어의 전체 경험치 기준으로 경험치 지급
     *
     * @param amount 지급 경험치
     */
    fun Player.setTotalExp(amount: Int) {
        this.level = 0
        this.exp = 0f
        this.totalExperience = 0
        this.giveExp(amount)
        this.stopSound(Sound.ENTITY_PLAYER_LEVELUP)
    }

    fun Location.dropExp(amount: Int) {
        if (amount <= 0) {
            return
        }

        val orb = this.world.spawnEntity(this, EntityType.EXPERIENCE_ORB) as ExperienceOrb
        orb.experience = amount
    }
}