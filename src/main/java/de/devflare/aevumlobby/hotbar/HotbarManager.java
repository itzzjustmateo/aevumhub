package de.devflare.aevumlobby.hotbar;

import lombok.Getter;
import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.config.ConfigManager;
import de.devflare.aevumlobby.service.Service;
import de.devflare.aevumlobby.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class HotbarManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private final Map<String, HotbarItem> items;
    private boolean giveOnJoin;
    private boolean giveOnRespawn;
    private boolean clearInventory;
    private boolean enabled;

    public HotbarManager(final AevumLobbyPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.items = new ConcurrentHashMap<>();
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing hotbar system...");
        this.loadSettings();
        this.registerDefaultItems();
    }

    @Override
    public void shutdown() {
        this.items.clear();
    }

    @Override
    public void reload() {
        this.items.clear();
        this.loadSettings();
        this.registerDefaultItems();
        this.plugin.getLogger().info("Hotbar system reloaded");
    }

    @Override
    public String getName() {
        return "HotbarManager";
    }

    @Override
    public int getPriority() {
        return 50;
    }

    private void loadSettings() {
        this.giveOnJoin = this.configManager.getBoolean("hotbar.give-on-join", true);
        this.giveOnRespawn = this.configManager.getBoolean("hotbar.give-on-respawn", true);
        this.clearInventory = this.configManager.getBoolean("hotbar.clear-inventory", true);
    }

    private void registerDefaultItems() {
        this.registerItem("profile", new SimpleHotbarItem(
                "profile",
                0,
                player -> new ItemBuilder(Material.PLAYER_HEAD)
                        .name(this.configManager.getString("hotbar.items.profile.name",
                                "<gradient:#ff6b6b:#ffd93d>Profile</gradient>"))
                        .lore(this.configManager.getStringList("hotbar.items.profile.lore"))
                        .build(),
                player -> {
                    this.plugin.getServiceRegistry()
                            .getService(de.devflare.aevumlobby.gui.GUIManager.class)
                            .ifPresent(gui -> gui.openGUI(player, "profile"));
                },
                this.configManager.getBoolean("hotbar.items.profile.enabled", true)));

        this.registerItem("navigator", new SimpleHotbarItem(
                "navigator",
                4,
                player -> new ItemBuilder(Material.COMPASS)
                        .name(this.configManager.getString("hotbar.items.navigator.name",
                                "<gradient:#4facfe:#00f2fe>Server Navigator</gradient>"))
                        .lore(this.configManager.getStringList("hotbar.items.navigator.lore"))
                        .build(),
                player -> {
                    this.plugin.getServiceRegistry()
                            .getService(de.devflare.aevumlobby.gui.GUIManager.class)
                            .ifPresent(gui -> gui.openGUI(player, "navigator"));
                },
                this.configManager.getBoolean("hotbar.items.navigator.enabled", true)));

        this.registerItem("visibility", new SimpleHotbarItem(
                "visibility",
                8,
                player -> {
                    final boolean visible = this.plugin.getServiceRegistry()
                            .getService(de.devflare.aevumlobby.visibility.VisibilityManager.class)
                            .map(vm -> vm.isVisible(player))
                            .orElse(true);

                    final Material material = visible ? Material.LIME_DYE : Material.GRAY_DYE;
                    final String name = visible ? "<green>Players Visible</green>" : "<gray>Players Hidden</gray>";

                    return new ItemBuilder(material)
                            .name(name)
                            .lore("<gray>Click to toggle player visibility")
                            .build();
                },
                player -> {
                    this.plugin.getServiceRegistry()
                            .getService(de.devflare.aevumlobby.visibility.VisibilityManager.class)
                            .ifPresent(vm -> vm.toggleVisibility(player));
                },
                this.configManager.getBoolean("hotbar.items.visibility.enabled", true)));
    }

    public void registerItem(final String identifier, final HotbarItem item) {
        this.items.put(identifier, item);
        this.plugin.getLogger().info("Registered hotbar item: " + identifier);
    }

    public Optional<HotbarItem> getItem(final String identifier) {
        return Optional.ofNullable(this.items.get(identifier));
    }

    public void giveHotbarItems(final Player player) {
        if (!this.enabled) {
            return;
        }

        if (this.clearInventory) {
            player.getInventory().clear();
        }

        for (final HotbarItem item : this.items.values()) {
            if (item.isEnabled()) {
                player.getInventory().setItem(item.getSlot(), item.getItem(player));
            }
        }

        player.updateInventory();
    }

    public Optional<HotbarItem> getItemBySlot(final int slot) {
        return this.items.values().stream()
                .filter(item -> item.getSlot() == slot)
                .findFirst();
    }

    public Optional<HotbarItem> getItemByItemStack(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return Optional.empty();
        }

        for (final HotbarItem item : this.items.values()) {
            final ItemStack hotbarItem = item.getItem(null);
            if (hotbarItem != null && hotbarItem.isSimilar(itemStack)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
