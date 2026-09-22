package space.byeoruk.lib.inventory.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import space.byeoruk.lib.inventory.utility.CustomAnvilInventory

class CustomAnvilInventoryListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onPrepareAnvil(event: PrepareAnvilEvent) {
        val inventory = CustomAnvilInventory.from(event.view) ?: return
        inventory.onPrepareAnvil(event)
    }

    @EventHandler
    private fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = CustomAnvilInventory.from(event.view) ?: return
        inventory.onInventoryClick(event)
    }

    @EventHandler
    private fun onInventoryClose(event: InventoryCloseEvent) {
        val inventory = CustomAnvilInventory.from(event.view) ?: return
        inventory.onInventoryClose(event)
        CustomAnvilInventory.unregister(event.view)
        inventory.inventory.clear()
    }
}
