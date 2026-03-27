package de.devflare.aevumlobby.listener;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.cache.CacheManager;
import de.devflare.aevumlobby.hotbar.HotbarManager;
import de.devflare.aevumlobby.scoreboard.ScoreboardManager;
import de.devflare.aevumlobby.spawn.SpawnManager;
import de.devflare.aevumlobby.visibility.VisibilityManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    private final AevumLobbyPlugin plugin;

    public PlayerJoinListener(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.plugin.getServiceRegistry().getService(CacheManager.class)
                .ifPresent(cache -> cache.getOrCreateCache(player.getUniqueId()));

        this.plugin.getServiceRegistry().getService(SpawnManager.class)
                .ifPresent(spawn -> {
                    if (this.plugin.getServiceRegistry().getService(de.devflare.aevumlobby.config.ConfigManager.class)
                            .map(config -> config.getBoolean("spawn.teleport-on-join", true))
                            .orElse(true)) {
                        spawn.teleportToSpawn(player);
                    }
                });

        this.plugin.getServiceRegistry().getService(HotbarManager.class)
                .ifPresent(hotbar -> {
                    if (hotbar.isEnabled()) {
                        hotbar.giveHotbarItems(player);
                    }
                });

        this.plugin.getServiceRegistry().getService(ScoreboardManager.class)
                .ifPresent(scoreboard -> {
                    if (scoreboard.isEnabled()) {
                        scoreboard.createScoreboard(player);
                    }
                });

        this.plugin.getServiceRegistry().getService(VisibilityManager.class)
                .ifPresent(visibility -> visibility.applyVisibility(player));
    }
}
