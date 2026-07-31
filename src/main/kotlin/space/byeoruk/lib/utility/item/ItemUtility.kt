package space.byeoruk.lib.utility.item

import dev.lone.itemsadder.api.CustomStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object ItemUtility {
    /**
     * 문자열로 아이템 스택 반환
     *
     * @param identifier 아이템 문자열
     * @return 아이템 스택
     */
    fun getItemStackByName(identifier: String, amount: Int = 1): ItemStack? {
        val trimmed = identifier.trim().lowercase()

        //  네임스페이스 구분
        val explode = trimmed.split(":", limit = 2)
        val namespace = if (explode.size > 1) explode[0] else "minecraft"
        val key = if (explode.size > 1) explode[1] else explode[0]

        return if (namespace == "minecraft") {
            val material = Material.matchMaterial(key) ?: return null
            ItemStack(material, amount)
        }
        else {
            val fullNamespacedId = "$namespace:$key"
            val customStack = CustomStack.getInstance(fullNamespacedId) ?: return null

            val itemStack = customStack.itemStack.clone()
            itemStack.amount = amount
            itemStack
        }
    }

    /**
     * Namespace가 포함된 아이템 이름 반환
     *
     * @return 아이템 이름
     */
    val ItemStack?.namespacedId: String?
        get() {
            if (this == null || type.isAir)
                return null

            val customStack = CustomStack.byItemStack(this)
            if (customStack != null) {
                return customStack.namespacedID
            }

            return type.key.toString()
        }

    /**
     * 해당 아이템이 ItemsAdder 아이템인지 여부 반환
     *
     * @return ItemsAdder 아이템일 경우 true 아니면 false 반환
     */
    fun ItemStack?.isCustomItem(): Boolean {
        return !(this == null || this.type.isAir) && CustomStack.byItemStack(this) != null
    }
}