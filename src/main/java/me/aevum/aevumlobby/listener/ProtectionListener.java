package me.aevum.aevumlobby.listener;

import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.protection.ProtectionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public final class ProtectionListener implements Listener {

    private final AevumLobbyPlugin plugin;

    public ProtectionListener(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        this.plugin.getServiceRegistry().getService(ProtectionManager.class).ifPresent(protection -> {
            if (protection.isDamageDisabled() && !protection.canBypass(player)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        this.plugin.getServiceRegistry().getService(ProtectionManager.class).ifPresent(protection -> {
            if (protection.isHungerDisabled() && !protection.canBypass(player)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();

        this.plugin.getServiceRegistry().getService(ProtectionManager.class).ifPresent(protection -> {
            if (protection.isBlockBreakDisabled() && !protection.canBypass(player)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        this.plugin.getServiceRegistry().getService(ProtectionManager.class).ifPresent(protection -> {
            if (protection.isBlockPlaceDisabled() && !protection.canBypass(player)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        this.plugin.getServiceRegistry().getService(ProtectionManager.class).ifPresent(protection -> {
            if (protection.isItemDropDisabled() && !protection.canBypass(player)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onItemPickup(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();

        this.plugin.getServiceRegistry().getService(ProtectionManager.class).ifPresent(protection -> {
            if (protection.isItemPickupDisabled() && !protection.canBypass(player)) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        this.plugin.getServiceRegistry().getService(ProtectionManager.class).ifPresent(protection -> {
            if (protection.isInventoryClickDisabled() && !protection.canBypass(player)) {
                event.setCancelled(true);
            }
        });
    }
}
