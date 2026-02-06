package me.aevum.aevumlobby.protection;

import lombok.Getter;
import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.config.ConfigManager;
import me.aevum.aevumlobby.service.Service;
import org.bukkit.entity.Player;

@Getter
public final class ProtectionManager implements Service {

    private final AevumLobbyPlugin plugin;
    private final ConfigManager configManager;
    private boolean disableDamage;
    private boolean disableHunger;
    private boolean disableBlockBreak;
    private boolean disableBlockPlace;
    private boolean disableItemDrop;
    private boolean disableItemPickup;
    private boolean disableInventoryClick;
    private String bypassPermission;
    private boolean enabled;

    public ProtectionManager(final AevumLobbyPlugin plugin, final ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.enabled = true;
    }

    @Override
    public void initialize() {
        this.plugin.getLogger().info("Initializing protection system...");
        this.loadSettings();
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void reload() {
        this.loadSettings();
        this.plugin.getLogger().info("Protection settings reloaded");
    }

    @Override
    public String getName() {
        return "ProtectionManager";
    }

    @Override
    public int getPriority() {
        return 40;
    }

    private void loadSettings() {
        this.disableDamage = this.configManager.getBoolean("protection.disable-damage", true);
        this.disableHunger = this.configManager.getBoolean("protection.disable-hunger", true);
        this.disableBlockBreak = this.configManager.getBoolean("protection.disable-block-break", true);
        this.disableBlockPlace = this.configManager.getBoolean("protection.disable-block-place", true);
        this.disableItemDrop = this.configManager.getBoolean("protection.disable-item-drop", true);
        this.disableItemPickup = this.configManager.getBoolean("protection.disable-item-pickup", true);
        this.disableInventoryClick = this.configManager.getBoolean("protection.disable-inventory-click", false);
        this.bypassPermission = this.configManager.getString("protection.bypass-permission", "aevumlobby.bypass");
    }

    public boolean canBypass(final Player player) {
        return player.hasPermission(this.bypassPermission);
    }

    public boolean isDamageDisabled() {
        return this.enabled && this.disableDamage;
    }

    public boolean isHungerDisabled() {
        return this.enabled && this.disableHunger;
    }

    public boolean isBlockBreakDisabled() {
        return this.enabled && this.disableBlockBreak;
    }

    public boolean isBlockPlaceDisabled() {
        return this.enabled && this.disableBlockPlace;
    }

    public boolean isItemDropDisabled() {
        return this.enabled && this.disableItemDrop;
    }

    public boolean isItemPickupDisabled() {
        return this.enabled && this.disableItemPickup;
    }

    public boolean isInventoryClickDisabled() {
        return this.enabled && this.disableInventoryClick;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
