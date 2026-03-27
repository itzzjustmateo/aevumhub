package de.devflare.aevumlobby.gui;

import de.devflare.aevumlobby.util.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseGUI implements GUI, InventoryHolder {

    private final String title;
    private final int size;
    protected final Map<Integer, Consumer<Player>> actions;

    public BaseGUI(final String title, final int size) {
        this.title = title;
        this.size = size;
        this.actions = new HashMap<>();
    }

    @Override
    public String getTitle(final Player player) {
        return this.title;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void open(final Player player) {
        player.openInventory(this.getInventory(player));
    }

    @Override
    public void close(final Player player) {
        player.closeInventory();
    }

    @Override
    public void refresh(final Player player) {
        // Default implementation ignores refresh, override if needed
    }

    @Override
    public @NotNull Inventory getInventory(final Player player) {
        // Create inventory with component title if possible, else legacy string
        final Component titleComponent = TextUtil.parse(this.getTitle(player));
        final Inventory inventory = Bukkit.createInventory(this, this.size, titleComponent);

        this.populate(player, inventory);

        return inventory;
    }

    protected abstract void populate(Player player, Inventory inventory);

    @Override
    public void handleClick(final Player player, final int slot) {
        if (this.actions.containsKey(slot)) {
            this.actions.get(slot).accept(player);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null; // Not used
    }
}
