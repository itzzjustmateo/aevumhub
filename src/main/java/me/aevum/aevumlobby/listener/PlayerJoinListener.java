package me.aevum.aevumlobby.listener;

import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.cache.CacheManager;
import me.aevum.aevumlobby.hotbar.HotbarManager;
import me.aevum.aevumlobby.scoreboard.ScoreboardManager;
import me.aevum.aevumlobby.spawn.SpawnManager;
import me.aevum.aevumlobby.visibility.VisibilityManager;
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
                    if (this.plugin.getServiceRegistry().getService(me.aevum.aevumlobby.config.ConfigManager.class)
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
