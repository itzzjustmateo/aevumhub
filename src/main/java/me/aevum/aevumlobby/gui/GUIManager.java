package me.aevum.aevumlobby.gui;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.config.ConfigManager;
import me.aevum.aevumlobby.service.Service;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class GUIManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private final Map<String, GUI> registeredGUIs;
    private final Map<UUID, GUI> openGUIs;
    private boolean enabled;

    public GUIManager(final AevumLobbyPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.registeredGUIs = new ConcurrentHashMap<>();
        this.openGUIs = new ConcurrentHashMap<>();
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing GUI system...");
    }

    @Override
    public void shutdown() {
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (this.openGUIs.containsKey(player.getUniqueId())) {
                player.closeInventory();
            }
        }
        this.openGUIs.clear();
        this.registeredGUIs.clear();
    }

    @Override
    public void reload() {
        this.plugin.getLogger().info("GUI system reloaded");
    }

    @Override
    public String getName() {
        return "GUIManager";
    }

    @Override
    public int getPriority() {
        return 70;
    }

    public void registerGUI(final String identifier, final GUI gui) {
        this.registeredGUIs.put(identifier, gui);
        this.plugin.getLogger().info("Registered GUI: " + identifier);
    }

    public Optional<GUI> getGUI(final String identifier) {
        return Optional.ofNullable(this.registeredGUIs.get(identifier));
    }

    public void openGUI(final Player player, final String identifier) {
        this.getGUI(identifier).ifPresent(gui -> {
            gui.open(player);
            this.openGUIs.put(player.getUniqueId(), gui);
        });
    }

    public void closeGUI(final Player player) {
        final GUI gui = this.openGUIs.remove(player.getUniqueId());
        if (gui != null) {
            gui.close(player);
        }
    }

    public Optional<GUI> getOpenGUI(final Player player) {
        return Optional.ofNullable(this.openGUIs.get(player.getUniqueId()));
    }

    public boolean hasGUIOpen(final Player player) {
        return this.openGUIs.containsKey(player.getUniqueId());
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
