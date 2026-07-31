package space.byeoruk.lib.utility.location

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace

object LocationUtility {
    /**
     * 이 위치가 안전한 위치인지 반환
     *
     * @return 안전한 위치일 경우 true 아니면 false 반환
     */
    fun Location.isSafe(): Boolean {
        val block = this.block
        val headBlock = block.getRelative(BlockFace.UP)
        val groundBlock = block.getRelative(BlockFace.DOWN)

        //  Y가 세계 밖인지 확인
        if (this.y < this.world.minHeight) {
            return false
        }

        //  몸과 머리가 들어갈 공간이 비어있는지 확인 (질식사 방지)
        if (!block.isPassable || !headBlock.isPassable) {
            return false
        }

        //  발을 디딜 바닥이 단단한 블록인지 확인 (허공 추락 방지)
        if (!groundBlock.type.isSolid) {
            return false
        }

        //  밟으면 위험한 블록
        val dangerBlocks = setOf(
            Material.LAVA,
            Material.FIRE,
            Material.SOUL_FIRE,
            Material.MAGMA_BLOCK,
            Material.CAMPFIRE,
            Material.SOUL_CAMPFIRE,
            Material.CACTUS,
            Material.SWEET_BERRY_BUSH,
            Material.WITHER_ROSE
        )

        if (groundBlock.type in dangerBlocks || block.type in dangerBlocks) {
            return false
        }

        return true
    }

    fun findSafeLocation(start: Location, radius: Int = 3): Location? {
        val world = start.world ?: return null

        if (start.isSafe()) {
            return start
        }

        var closestLocation: Location? = null
        var closestDistance = Double.MAX_VALUE

        //  지정된 범위를 돌며 탐색
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    //  탐색 후보 위치 생성
                    val candidate = start.clone().add(x.toDouble(), y.toDouble(), z.toDouble())

                    candidate.x = candidate.blockX + .5
                    candidate.y = candidate.blockY + .5

                    if (candidate.isSafe()) {
                        //  가장 가까운 거리에 있는 안전한 위치 선택
                        val distance = start.distanceSquared(candidate)
                        if (distance < closestDistance) {
                            closestDistance = distance
                            closestLocation = candidate
                        }
                    }
                }
            }
        }

        return closestLocation
    }
}