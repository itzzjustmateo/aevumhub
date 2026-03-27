package de.devflare.aevumlobby.listener;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.cache.CacheManager;
import de.devflare.aevumlobby.gui.GUIManager;
import de.devflare.aevumlobby.scoreboard.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitListener implements Listener {

    private final AevumLobbyPlugin plugin;

    public PlayerQuitListener(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.plugin.getServiceRegistry().getService(CacheManager.class)
                .ifPresent(cache -> cache.removeCache(player.getUniqueId()));

        this.plugin.getServiceRegistry().getService(ScoreboardManager.class)
                .ifPresent(scoreboard -> scoreboard.removeScoreboard(player));

        this.plugin.getServiceRegistry().getService(GUIManager.class)
                .ifPresent(gui -> gui.closeGUI(player));
    }
}
