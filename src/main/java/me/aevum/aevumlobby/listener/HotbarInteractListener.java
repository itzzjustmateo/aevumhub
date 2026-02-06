package me.aevum.aevumlobby.listener;

import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.hotbar.HotbarManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public final class HotbarInteractListener implements Listener {

    private final AevumLobbyPlugin plugin;

    public HotbarInteractListener(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (event.getItem() == null) {
            return;
        }

        this.plugin.getServiceRegistry().getService(HotbarManager.class).ifPresent(hotbar -> {
            hotbar.getItemByItemStack(event.getItem()).ifPresent(item -> {
                event.setCancelled(true);
                item.onClick(player);
            });
        });
    }
}
