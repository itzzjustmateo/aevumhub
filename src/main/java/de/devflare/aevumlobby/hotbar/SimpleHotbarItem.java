package de.devflare.aevumlobby.hotbar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public final class SimpleHotbarItem implements HotbarItem {

    private final String identifier;
    private final int slot;
    private final Function<Player, ItemStack> itemProvider;
    private final Consumer<Player> clickHandler;
    private final boolean enabled;

    @Override
    public ItemStack getItem(final Player player) {
        return this.itemProvider.apply(player);
    }

    @Override
    public void onClick(final Player player) {
        this.clickHandler.accept(player);
    }
}
