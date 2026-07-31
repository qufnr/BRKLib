package space.byeoruk.lib.utility.item

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.block.banner.PatternType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import space.byeoruk.lib.builder.item.ItemBuilder

object IconItem {
    private val mm = MiniMessage.miniMessage()

    const val LORE_PREFIX = "<!italic><dark_grey> · <grey>"
    const val ACTION_PREFIX = "<italic><dark_grey>"

    /**
     * 이전 아이콘
     *
     * @param disabled 비활성화 여부
     * @return 아이콘 아이템
     */
    fun previousIcon(disabled: Boolean = false): ItemStack {
        val material = if (disabled) Material.RED_BANNER else Material.RED_BANNER
        val name = if (disabled) mm.deserialize("<!italic><grey>이전") else mm.deserialize("<!italic><white>이전")
        val colour = if (disabled) DyeColor.RED else DyeColor.BLACK

        return ItemBuilder(material)
            .displayName(name)
            .addBannerPattern(DyeColor.WHITE, PatternType.RHOMBUS)
            .addBannerPattern(DyeColor.WHITE, PatternType.HALF_VERTICAL)
            .addBannerPattern(colour, PatternType.SQUARE_TOP_RIGHT)
            .addBannerPattern(colour, PatternType.SQUARE_BOTTOM_RIGHT)
            .addBannerPattern(colour, PatternType.TRIANGLES_TOP)
            .addBannerPattern(colour, PatternType.TRIANGLES_BOTTOM)
            .addBannerPattern(colour, PatternType.BORDER)
            .build()
    }

    /**
     * 다음 아이콘
     *
     * @param disabled 비활성화 여부
     * @return 아이콘 아이템
     */
    fun nextIcon(disabled: Boolean = false): ItemStack {
        val material = if (disabled) Material.RED_BANNER else Material.BLACK_BANNER
        val name = if (disabled) mm.deserialize("<!italic><grey>다음") else mm.deserialize("<!italic><white>다음")
        val colour = if (disabled) DyeColor.RED else DyeColor.BLACK

        return ItemBuilder(material)
            .displayName(name)
            .addBannerPattern(DyeColor.WHITE, PatternType.RHOMBUS)
            .addBannerPattern(DyeColor.WHITE, PatternType.HALF_VERTICAL)
            .addBannerPattern(colour, PatternType.SQUARE_TOP_LEFT)
            .addBannerPattern(colour, PatternType.SQUARE_BOTTOM_LEFT)
            .addBannerPattern(colour, PatternType.TRIANGLES_TOP)
            .addBannerPattern(colour, PatternType.TRIANGLES_BOTTOM)
            .addBannerPattern(colour, PatternType.BORDER)
            .build()
    }

    /**
     * 새로고침 아이콘
     *
     * @return 아이콘 아이템
     */
    fun refreshIcon(): ItemStack = ItemBuilder(Material.WHITE_BANNER)
        .displayName(mm.deserialize("<!italic>새로고침"))
        .lore(listOf(
            mm.deserialize("<reset> "),
            mm.deserialize("${ACTION_PREFIX}클릭해서 새로고쳐요")
        ))
        .addBannerPattern(DyeColor.BLACK, PatternType.FLOW)
        .build()

    /**
     * 머리 아이콘
     *
     * @param player 머리 대상
     * @return 아이콘 아이템
     */
    fun headIcon(player: Player): ItemStack = ItemBuilder(Material.PLAYER_HEAD)
        .playerProfile(player)
        .build()

    /**
     * 머리 아이콘
     *
     * @param textures 머리 텍스쳐
     * @return 아이콘 아이템
     */
    fun headIcon(textures: String): ItemStack = ItemBuilder(Material.PLAYER_HEAD)
        .playerProfile(textures)
        .build()
}