package space.byeoruk.lib.builder.item

import com.destroystokyo.paper.profile.ProfileProperty
import dev.lone.itemsadder.api.CustomStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.DyeColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import java.util.UUID

class ItemBuilder {
    private val item: ItemStack
    private val meta: ItemMeta

    constructor(material: Material, amount: Int = 1) {
        item = ItemStack(material, amount)
        meta = item.itemMeta
    }

    constructor(item: ItemStack, amount: Int = 1) {
        this.item = item
        this.item.amount = amount
        meta = item.itemMeta
    }

    constructor(identifier: String, amount: Int = 1) {
        val trimmed = identifier.trim()

        if (trimmed.startsWith("minecraft:")) {
            val material = Material.matchMaterial(trimmed) ?:
                throw RuntimeException("\"$trimmed\" 아이템은 존재하지 않아요")
            item = ItemStack(material, amount)
            meta = item.itemMeta
        }
        else {
            val customStack = CustomStack.getInstance(trimmed)
                ?: throw RuntimeException("\"$trimmed\" 아이템은 존재하지 않아요. ItemsAdder 구성을 확인해 주세요")
            item = customStack.itemStack
            item.amount = amount
            meta = item.itemMeta
        }
    }

    constructor(customName: String, namespace: String, amount: Int = 1) {
        val customStack = CustomStack.getInstance("$namespace:$customName")
            ?: throw RuntimeException("\"$namespace:$customName\" 아이템은 존재하지 않아요. ItemsAdder 구성을 확인해 주세요")
        item = customStack.itemStack
        item.amount = amount
        meta = item.itemMeta
    }

    /**
     * 표시 이름 설정
     *
     * @param component 이름
     * @return ItemBuilder
     */
    fun displayName(component: Component): ItemBuilder {
        meta.displayName(component)
        return this
    }

    fun displayName(name: String): ItemBuilder {
        val mm = MiniMessage.miniMessage()
        meta.displayName(mm.deserialize(name))
        return this
    }

    /**
     * Lore 설정
     *
     * @param elements 로어 내용
     * @return ItemBuilder
     */
    @JvmName("loreComponent")
    fun lore(elements: List<Component>): ItemBuilder {
        var lore = meta.lore()
        if (lore == null) {
            lore = elements
        }
        else {
            lore.addAll(elements)
        }
        meta.lore(lore)
        return this
    }

    @JvmName("loreString")
    fun lore(elements: List<String>): ItemBuilder {
        val mm = MiniMessage.miniMessage()
        val components = elements.map { mm.deserialize(it) }
        var lore = meta.lore()
        if (lore == null) {
            lore = components
        }
        else {
            lore.addAll(components)
        }
        meta.lore(lore)
        return this
    }

    /**
     * 아이템 빛나게 설정
     *
     * @param enabled 활성화 여부
     * @return ItemBuilder
     */
    fun glowing(enabled: Boolean = true): ItemBuilder {
        meta.setEnchantmentGlintOverride(enabled)
        return this
    }

    /**
     * 배너 아이템 패턴 추가
     *
     * @param color 색상
     * @param pattern 패턴 유형
     * @return ItemBuilder
     */
    fun addBannerPattern(color: DyeColor, pattern: PatternType): ItemBuilder {
        val bannerMeta = meta as? BannerMeta ?: return this
        bannerMeta.addPattern(Pattern(color, pattern))
        return this
    }

    /**
     * 자석석 목적지 설정
     *
     * @param location 목적지
     */
    fun lodestone(location: Location): ItemBuilder {
        val compassMeta = meta as? CompassMeta ?: return this
        compassMeta.lodestone = location
        return this
    }

    /**
     * 아이템 플래그 설정
     *
     * @param flag 플래그
     * @return ItemBuilder
     */
    fun itemFlags(flag: ItemFlag): ItemBuilder {
        meta.addItemFlags(flag)
        return this
    }

    /**
     * 머리 아이템 플레이어 프로필 설정
     *
     * @param player 플레이어
     * @return ItemBuilder
     */
    fun playerProfile(player: Player): ItemBuilder {
        if (item.type == Material.PLAYER_HEAD && meta is SkullMeta) {
            meta.playerProfile = player.playerProfile
        }
        return this
    }

    /**
     * 머리 아이템 플레이어 프로필 설정
     *
     * @param textures 텍스쳐 문자열
     * @return ItemBuilder
     */
    fun playerProfile(textures: String): ItemBuilder {
        if (item.type == Material.PLAYER_HEAD && meta is SkullMeta) {
            val uuid = UUID.randomUUID()
            val playerProfile = Bukkit.createProfile(uuid.toString().substring(0, 16))
            playerProfile.setProperty(ProfileProperty("textures", textures))
            meta.playerProfile = playerProfile
        }
        return this
    }

    fun build(): ItemStack {
        item.itemMeta = meta
        return item
    }
}