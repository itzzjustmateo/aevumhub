package me.aevum.aevumlobby.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface GUI {

    String getTitle(Player player);

    int getSize();

    void open(Player player);

    void close(Player player);

    void refresh(Player player);

    Inventory getInventory(Player player);

    void handleClick(Player player, int slot);
}
