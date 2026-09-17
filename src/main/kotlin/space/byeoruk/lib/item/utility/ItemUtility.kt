package space.byeoruk.lib.item.utility

import dev.lone.itemsadder.api.CustomStack
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import space.byeoruk.lib.MainPlugin
import java.util.Locale

object ItemUtility {
    const val LORE_PREFIX = "<!italic><#81D4FA>| </#81D4FA><#E1F5FE>"           //  200, 50
    const val LORE_ERROR_PREFIX = "<!italic><#EF9A9A>| </#EF9A9A><#FBE9E7>"   //  red 200, 50
    const val ACTION_PREFIX = "<!italic><#0091EA>| </#0091EA><#80D8FF>"       //  A700 / A100
    const val ACTION_ERROR_PREFIX = "<!italic><#DD2C00>| </#DD2C00><#FF9E80>" //  red A700 / A100
    const val DIVIDE = "<!italic><#01579B><st>--------------------------</st><#01579B>"

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
    fun ItemStack?.isCustomItem(): Boolean =
        !(this == null || this.type.isAir) && CustomStack.byItemStack(this) != null

    /**
     * 해당 언어로 보이는 이름을 평문으로 반환
     *
     * @param locale 언어
     * @return 아이템 이름 평문 문자열
     */
    fun ItemStack?.nameOf(locale: Locale): String? =
        if (this != null)
            MainPlugin.plugin.itemLanguageManager.nameOf(this, locale)
        else
            null
}