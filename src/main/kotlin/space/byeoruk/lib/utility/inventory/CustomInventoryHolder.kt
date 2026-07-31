package space.byeoruk.lib.utility.inventory

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

interface CustomInventoryHolder : InventoryHolder {
    fun onOpen() {}
    fun onClose() {}
    fun onClick(item: ItemStack, slot: Int): Boolean = true

    fun onInventoryOpen(event: InventoryOpenEvent) {
        onOpen()
    }

    fun onInventoryClose(event: InventoryCloseEvent) {
        onClose()
    }

    fun onInventoryClick(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        val slot = event.rawSlot
        event.isCancelled = onClick(item, slot)
    }
}