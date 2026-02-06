package me.aevum.aevumlobby.visibility;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.cache.CacheManager;
import me.aevum.aevumlobby.cache.PlayerCache;
import me.aevum.aevumlobby.config.ConfigManager;
import me.aevum.aevumlobby.service.Service;
import me.aevum.aevumlobby.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public final class VisibilityManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private final CacheManager cacheManager;
    private boolean defaultVisible;
    private boolean savePreferences;
    private String bypassPermission;
    private boolean enabled;

    public VisibilityManager(final AevumLobbyPlugin plugin, final ConfigManager configManager, final CacheManager cacheManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.cacheManager = cacheManager;
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing visibility system...");
        this.loadSettings();
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void reload() {
        this.loadSettings();
        this.plugin.getLogger().info("Visibility system reloaded");
    }

    @Override
    public String getName() {
        return "VisibilityManager";
    }

    @Override
    public int getPriority() {
        return 60;
    }

    private void loadSettings() {
        this.defaultVisible = this.configManager.getString("visibility.default-state", "VISIBLE").equalsIgnoreCase("VISIBLE");
        this.savePreferences = this.configManager.getBoolean("visibility.save-preferences", true);
        this.bypassPermission = this.configManager.getString("visibility.bypass-permission", "aevumlobby.staff");
    }

    public void setVisible(final Player player, final boolean visible) {
        final PlayerCache cache = this.cacheManager.getOrCreateCache(player.getUniqueId());
        cache.setPlayersVisible(visible);

        this.updateVisibility(player, visible);

        final String message = visible
                ? this.configManager.getLanguageString("messages.visibility-enabled", "<green>Players are now visible</green>")
                : this.configManager.getLanguageString("messages.visibility-disabled", "<gray>Players are now hidden</gray>");

        player.sendMessage(TextUtil.parse(message));
    }

    public void toggleVisibility(final Player player) {
        final boolean currentlyVisible = this.isVisible(player);
        this.setVisible(player, !currentlyVisible);
    }

    public boolean isVisible(final Player player) {
        return this.cacheManager.getCache(player.getUniqueId())
                .map(PlayerCache::isPlayersVisible)
                .orElse(this.defaultVisible);
    }

    private void updateVisibility(final Player player, final boolean visible) {
        if (visible) {
            for (final Player online : Bukkit.getOnlinePlayers()) {
                if (!online.equals(player) && !online.hasPermission(this.bypassPermission)) {
                    player.showPlayer(this.plugin, online);
                }
            }
        } else {
            for (final Player online : Bukkit.getOnlinePlayers()) {
                if (!online.equals(player) && !online.hasPermission(this.bypassPermission)) {
                    player.hidePlayer(this.plugin, online);
                }
            }
        }
    }

    public void applyVisibility(final Player player) {
        final boolean visible = this.isVisible(player);
        this.updateVisibility(player, visible);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
