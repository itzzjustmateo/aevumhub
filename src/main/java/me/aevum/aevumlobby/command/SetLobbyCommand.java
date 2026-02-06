package me.aevum.aevumlobby.command;

import me.aevum.aevumlobby.AevumLobbyPlugin;
import me.aevum.aevumlobby.spawn.SpawnManager;
import me.aevum.aevumlobby.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SetLobbyCommand implements CommandExecutor {

    private final AevumLobbyPlugin plugin;

    public SetLobbyCommand(final AevumLobbyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("aevumlobby.admin")) {
            player.sendMessage(TextUtil.parse("<red>You don't have permission to use this command.</red>"));
            return true;
        }

        final SpawnManager spawnManager = this.plugin.getServiceRegistry()
                .getService(SpawnManager.class)
                .orElse(null);

        if (spawnManager == null) {
            player.sendMessage(TextUtil.parse("<red>Spawn manager is not available."));
            return true;
        }

        spawnManager.setSpawn("lobby", player.getLocation());
        player.sendMessage(TextUtil.parse("<green>Lobby spawn has been set!"));

        return true;
    }
}
