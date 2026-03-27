package de.devflare.aevumlobby.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface HotbarItem {

    ItemStack getItem(Player player);

    void onClick(Player player);

    int getSlot();

    String getIdentifier();

    boolean isEnabled();
}
