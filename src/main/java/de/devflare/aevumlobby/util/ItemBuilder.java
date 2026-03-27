package de.devflare.aevumlobby.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(final Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(final ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder name(final String name) {
        if (this.itemMeta != null) {
            this.itemMeta.displayName(TextUtil.parse(name));
        }
        return this;
    }

    public ItemBuilder name(final Component component) {
        if (this.itemMeta != null) {
            this.itemMeta.displayName(component);
        }
        return this;
    }

    public ItemBuilder lore(final String... lines) {
        return this.lore(Arrays.asList(lines));
    }

    public ItemBuilder lore(final List<String> lines) {
        if (this.itemMeta != null) {
            final List<Component> lore = new ArrayList<>();
            for (final String line : lines) {
                lore.add(TextUtil.parse(line));
            }
            this.itemMeta.lore(lore);
        }
        return this;
    }

    public ItemBuilder amount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(final Enchantment enchantment, final int level) {
        if (this.itemMeta != null) {
            this.itemMeta.addEnchant(enchantment, level, true);
        }
        return this;
    }

    public ItemBuilder flag(final ItemFlag... flags) {
        if (this.itemMeta != null) {
            this.itemMeta.addItemFlags(flags);
        }
        return this;
    }

    public ItemBuilder unbreakable(final boolean unbreakable) {
        if (this.itemMeta != null) {
            this.itemMeta.setUnbreakable(unbreakable);
        }
        return this;
    }

    public ItemBuilder glow() {
        return this.enchant(Enchantment.LURE, 1)
                .flag(ItemFlag.HIDE_ENCHANTS);
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
