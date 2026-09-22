package space.byeoruk.lib.inventory.utility

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MenuType
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.view.AnvilView
import java.util.UUID

/**
 * MenuType 기반 모루 GUI.
 *
 * MenuType 으로 만든 뷰에는 InventoryHolder 가 없기 때문에,
 * 열려 있는 인스턴스를 [CustomAnvilInventory.Companion] 레지스트리에 등록해 식별한다.
 *
 * 구현체는 [view] 를 반드시 `by lazy` 로 선언해 뷰 하나를 재사용해야 한다.
 *
 * ```
 * class RenameInventory(override val viewer: Player) : CustomAnvilInventory {
 *     override val view by lazy { createView(Component.text("모루루")) }
 *
 *     override fun onPrepareAnvil(event: PrepareAnvilEvent) { ... }
 * }
 * ```
 */
interface CustomAnvilInventory {
    val viewer: Player

    /**
     * 이 GUI 의 모루 뷰.
     *
     * 접근할 때마다 새 뷰가 만들어지지 않도록 구현체에서
     * `override val view by lazy { createView() }` 형태로 선언한다.
     */
    val view: AnvilView

    /** 모루 상단 인벤토리. (0: 첫번째 슬롯, 1: 두번째 슬롯, 2: 결과 슬롯) */
    val inventory: AnvilInventory get() = view.topInventory

    /** 플레이어가 입력한 이름. */
    val renameText: String get() = view.renameText ?: ""

    /**
     * 뷰 생성. 구현체의 `by lazy` 초기화에서 호출한다.
     *
     * @param title 뷰 제목
     * @return 모루 뷰
     */
    fun createView(title: Component): AnvilView = MenuType.ANVIL
        .builder()
        .title(title)
        .checkReachable(false)
        .build(viewer)

    fun open() {
        // 이전에 열려 있던 GUI 의 InventoryCloseEvent 가 open() 도중 발생하므로,
        // 등록은 반드시 open() 이후에 한다.
        view.open()
        register(viewer.uniqueId, this)
    }

    fun onPrepareAnvil(event: PrepareAnvilEvent) {}

    fun onInventoryClick(event: InventoryClickEvent) {}

    fun onInventoryClose(event: InventoryCloseEvent) {}

    companion object {
        /** 플레이어 UUID -> 현재 열려 있는 GUI. 메인 스레드에서만 접근 */
        private val customAnvilViewers = mutableMapOf<UUID, CustomAnvilInventory>()

        /**
         * 해당 뷰가 CustomAnvilInventory 로 만들어진 인벤토리인지 확인한다.
         *
         * @return 모루 인벤토리 인스턴스, 아니라면 null 반환
         */
        fun from(view: InventoryView): CustomAnvilInventory? {
            val uuid = (view.player as? Player)?.uniqueId ?: return null
            return customAnvilViewers[uuid]?.takeIf { it.view === view }
        }

        /**
         * 플레이어가 열고 있는 모루 인벤토리 반환
         *
         * @param player 플레이어
         * @return 모루 인벤토리 인스턴스, 열려있지 않다면 null 반환
         */
        fun of(player: Player): CustomAnvilInventory? = customAnvilViewers[player.uniqueId]

        /**
         * CustomAnvilInventory 가 열릴 때 인스턴스를 등록한다.
         *
         * @param uuid 인벤토리가 열린 플레이어 UUID
         * @param inventory 모루 인벤토리 인스턴스
         */
        internal fun register(uuid: UUID, inventory: CustomAnvilInventory) {
            customAnvilViewers[uuid] = inventory
        }

        /**
         * CustomAnvilInventory 가 닫힐 때 인스턴스를 지운다.
         *
         * @param view 인벤토리 View 객체
         */
        internal fun unregister(view: InventoryView) {
            val uuid = (view.player as? Player)?.uniqueId ?: return

            if (customAnvilViewers[uuid]?.view === view) {
                customAnvilViewers.remove(uuid)
            }
        }

        /**
         * CustomAnvilInventory 가 열려있는 모든 플레이어를 강제로 닫고, 인스턴스를 죽인다
         *
         * 이 작업은 플러그인이 비활성화됐을 때 처리해야 함
         */
        internal fun unregisterAll() {
            customAnvilViewers.values.toList().forEach { customAnvil ->
                customAnvil.inventory.clear()
                customAnvil.viewer.closeInventory()
            }

            customAnvilViewers.clear()
        }
    }
}
