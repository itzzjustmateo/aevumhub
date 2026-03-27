package de.devflare.aevumlobby.gui;

import lombok.Getter;
import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.config.ConfigManager;
import de.devflare.aevumlobby.service.Service;
import org.bukkit.configuration.ConfigurationSection;
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
        this.loadMenus();
    }

    private void loadMenus() {
        this.registeredGUIs.clear();
        final ConfigurationSection menus = this.configManager.getMenus().getConfigurationSection("menus");
        if (menus == null) {
            this.plugin.getLogger().warning("No menus found in menus.yml!");
            return;
        }

        for (final String key : menus.getKeys(false)) {
            final ConfigurationSection menuSection = menus.getConfigurationSection(key);
            if (menuSection != null) {
                this.registerGUI(key, new ConfigurableGUI(this.plugin, menuSection));
            }
        }
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
        this.loadMenus();
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
