package space.byeoruk.lib.utility.inventory

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder

interface CustomInventoryHolder : InventoryHolder {
    fun onInventoryOpen(event: InventoryOpenEvent) {
    }

    fun onInventoryClose(event: InventoryCloseEvent) {
    }

    fun onInventoryClick(event: InventoryClickEvent) {
    }
}