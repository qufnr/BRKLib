package space.byeoruk.lib.inventory

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class CustomInventoryListener : Listener {
    @EventHandler
    private fun onInventoryOpen(event: InventoryOpenEvent) {
        val inventory = event.inventory.holder as? CustomInventoryHolder ?: return
        inventory.onInventoryOpen(event)
    }

    @EventHandler
    private fun onInventoryClose(event: InventoryCloseEvent) {
        val inventory = event.inventory.holder as? CustomInventoryHolder ?: return
        inventory.onInventoryClose(event)
    }

    @EventHandler
    private fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory.holder as? CustomInventoryHolder ?: return
        inventory.onInventoryClick(event)
    }
}