package de.devflare.aevumlobby.listener;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.gui.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class GUIListener implements Listener {

    private final AevumLobbyPlugin plugin;

    public GUIListener(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        this.plugin.getServiceRegistry().getService(GUIManager.class).ifPresent(guiManager -> {
            if (guiManager.hasGUIOpen(player)) {
                event.setCancelled(true);
                guiManager.getOpenGUI(player).ifPresent(gui -> gui.handleClick(player, event.getSlot()));
            }
        });
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        this.plugin.getServiceRegistry().getService(GUIManager.class).ifPresent(guiManager -> {
            if (guiManager.hasGUIOpen(player)) {
                guiManager.closeGUI(player);
            }
        });
    }
}
