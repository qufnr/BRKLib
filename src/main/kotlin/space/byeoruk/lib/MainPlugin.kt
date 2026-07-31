package space.byeoruk.lib

import org.bukkit.plugin.java.JavaPlugin
import space.byeoruk.lib.utility.inventory.CustomInventoryListener

class MainPlugin : JavaPlugin() {

    override fun onEnable() {
        server.pluginManager.registerEvents(CustomInventoryListener(), this)
    }
}
