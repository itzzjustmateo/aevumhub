package de.devflare.aevumlobby.command;

import de.devflare.aevumlobby.AevumLobbyPlugin;
import de.devflare.aevumlobby.spawn.SpawnManager;
import de.devflare.aevumlobby.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class HubCommand implements CommandExecutor {

    private final AevumLobbyPlugin plugin;

    public HubCommand(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        final SpawnManager spawnManager = this.plugin.getServiceRegistry()
                .getService(SpawnManager.class)
                .orElse(null);

        if (spawnManager == null) {
            player.sendMessage(TextUtil.parse("<red>Spawn manager is not available."));
            return true;
        }

        if (spawnManager.hasSpawn("hub")) {
            spawnManager.teleportToSpawn(player, "hub");
            player.sendMessage(TextUtil.parse("<green>Teleporting to hub..."));
        } else if (spawnManager.hasSpawn("lobby")) {
            spawnManager.teleportToSpawn(player, "lobby");
            player.sendMessage(TextUtil.parse("<green>Teleporting to lobby..."));
        } else {
            spawnManager.teleportToSpawn(player);
            player.sendMessage(TextUtil.parse("<green>Teleporting to spawn..."));
        }

        return true;
    }
}
