package space.byeoruk.lib

import org.bukkit.plugin.java.JavaPlugin
import space.byeoruk.lib.inventory.listener.CustomInventoryListener
import space.byeoruk.lib.item.manager.ItemLanguageManager

internal class MainPlugin : JavaPlugin() {
    lateinit var itemLanguageManager: ItemLanguageManager
        private set

    override fun onEnable() {
        instance = this

        registerManagers()
        registerEvents()
    }

    override fun onDisable() {
        instance = null
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(CustomInventoryListener(), this)
    }

    private fun registerManagers() {
        itemLanguageManager = ItemLanguageManager(this)
        itemLanguageManager.loadTranslations()
    }

    companion object {
        @Volatile
        private var instance: MainPlugin? = null

        /**
         * 활성화된 BRKLib 인스턴스.
         *
         * @return 플러그인 인스턴스
         * @throws IllegalStateException onEnable 이전이거나 onDisable 이후에 접근한 경우
         */
        val plugin: MainPlugin
            get() = instance ?: error("BRKLib is not enabled yet. Access it after onEnable().")
    }
}
