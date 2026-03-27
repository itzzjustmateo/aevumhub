package de.devflare.aevumlobby.listener;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.hotbar.HotbarManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class HotbarInteractListener implements Listener {

    private final AevumLobbyPlugin plugin;

    public HotbarInteractListener(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getItem() == null) {
            return;
        }

        final int slot = event.getPlayer().getInventory().getHeldItemSlot();

        this.plugin.getServiceRegistry().getService(HotbarManager.class).ifPresent(hotbar -> {
            hotbar.getItemBySlot(slot).ifPresent(item -> {
                // Verify item type matches to preventing using slot for other items
                if (event.getItem().getType() == item.getItem(event.getPlayer()).getType()) {
                    event.setCancelled(true);
                    item.onClick(event.getPlayer());
                }
            });
        });
    }
}
