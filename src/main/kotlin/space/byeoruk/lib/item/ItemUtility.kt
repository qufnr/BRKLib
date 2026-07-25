package space.byeoruk.lib.item

import dev.lone.itemsadder.api.CustomStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ItemUtility {
    /**
     * 문자열로 아이템 스택 반환
     *
     * @param name 아이템 문자열
     * @return 아이템 스택
     */
    fun getItemStackByName(name: String): ItemStack? =
        if (name.lowercase().startsWith("minecraft:")) {
            val material = Material.getMaterial(name.lowercase())
            if (material != null)
                ItemStack(material)
            else
                null
        }
        else if (name.lowercase().contains(":"))
            CustomStack.getInstance(name)?.itemStack
        else
            null

    /**
     * 해당 아이템이 ItemsAdder 아이템인지 여부 반환
     *
     * @return ItemsAdder 아이템일 경우 true 아니면 false 반환
     */
    fun ItemStack?.isCustomItem(): Boolean {
        if (this == null || this.type.isAir) {
            return false
        }

        return CustomStack.byItemStack(this) != null
    }
}