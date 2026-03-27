package de.devflare.aevumlobby.gui;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.util.ItemBuilder;
import de.devflare.aevumlobby.util.TextUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConfigurableGUI extends BaseGUI {

    private final ConfigurationSection section;
    private final AevumLobbyPlugin plugin;

    public ConfigurableGUI(final AevumLobbyPlugin plugin, final ConfigurationSection section) {
        super(section.getString("title", "GUI"), section.getInt("size", 27));
        this.plugin = plugin;
        this.section = section;
    }

    @Override
    protected void populate(final Player player, final Inventory inventory) {
        final ConfigurationSection items = this.section.getConfigurationSection("items");
        if (items == null) {
            return;
        }

        for (final String key : items.getKeys(false)) {
            final ConfigurationSection itemSection = items.getConfigurationSection(key);
            if (itemSection == null) {
                continue;
            }

            final int slot = itemSection.getInt("slot");
            final String materialName = itemSection.getString("material", "STONE");
            final String name = itemSection.getString("name", "Item");
            final List<String> lore = itemSection.getStringList("lore");
            final String action = itemSection.getString("action", "NONE");
            final String actionValue = itemSection.getString("server"); // For SERVER action

            final Material material = Material.matchMaterial(materialName);
            if (material == null) {
                continue;
            }

            final ItemStack item = new ItemBuilder(material)
                    .name(name)
                    .lore(lore)
                    .build();

            inventory.setItem(slot, item);

            // Register action
            if (!"NONE".equalsIgnoreCase(action)) {
                this.actions.put(slot, p -> {
                    switch (action.toUpperCase()) {
                        case "CLOSE":
                            p.closeInventory();
                            break;
                        case "SERVER":
                            if (actionValue != null) {
                                // Connect to server
                                p.sendMessage(TextUtil.parse("<green>Connecting to " + actionValue + "..."));
                                // Helper method to send plugin message would go here
                                // For now just message
                            }
                            break;
                        case "COMMAND":
                            final String command = itemSection.getString("command");
                            if (command != null) {
                                p.performCommand(command);
                            }
                            break;
                    }
                });
            }
        }
    }
}
